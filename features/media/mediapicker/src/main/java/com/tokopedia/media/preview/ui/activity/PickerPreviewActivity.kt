package com.tokopedia.media.preview.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ActivityPreviewBinding
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.preview.analytics.PREVIEW_PAGE_LANJUT
import com.tokopedia.media.preview.analytics.PREVIEW_PAGE_UPLOAD
import com.tokopedia.media.preview.analytics.PREVIEW_RETAKE_CAMMERA
import com.tokopedia.media.preview.analytics.PREVIEW_RETAKE_GALLERY
import com.tokopedia.media.preview.analytics.PREVIEW_RETAKE_RECORDER
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalyticsImpl
import com.tokopedia.media.preview.di.DaggerPreviewComponent
import com.tokopedia.media.preview.di.module.PreviewModule
import com.tokopedia.media.preview.ui.component.PreviewPagerComponent
import com.tokopedia.picker.common.EXTRA_EDITOR
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.picker.common.RESULT_PICKER
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.safeFileDelete
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class PickerPreviewActivity : BaseActivity()
    , NavToolbarComponent.Listener
    , DrawerSelectionWidget.Listener {

    @Inject lateinit var param: ParamCacheManager
    @Inject lateinit var previewAnalytics: PreviewAnalytics

    private val binding: ActivityPreviewBinding? by viewBinding()
    private val uiModel = arrayListOf<MediaUiModel>()

    // variable for control index item to be rendered first
    private var drawerIndexSelected = 0

    private val navToolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it
        )
    }

    private val pickerPager by uiComponent {
        PreviewPagerComponent(
            parent = it
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[PreviewViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        restoreDataState(savedInstanceState)
        initInjector()
        initView()
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
    }

    override fun onCloseClicked() {
        previewAnalytics.clickBackButton()
        onBackPickerIntent()
    }

    override fun onBackPressed() {
        if (param.get().isMultipleSelectionType()) {
            onBackPickerIntent()
        } else {
            onCancelOrRetakeMedia(uiModel.first())
        }
    }

    override fun onContinueClicked() {
        val isWithEditor = param.get().withEditor()
        val buttonState = if (isWithEditor) {
            PREVIEW_PAGE_LANJUT
        } else {
            PREVIEW_PAGE_UPLOAD
        }
        previewAnalytics.clickNextButton(buttonState)

        val result = uiModel
            .map { it.path }
            .map {
                viewModel.dispatchFileToPublic(
                    context = applicationContext,
                    filePath = it
                )

                it
            }
            .toList()

        val resultArrayList = ArrayList(result)

        if (isWithEditor) {
            onEditorIntent(resultArrayList)
        } else {
            onFinishIntent(resultArrayList)
        }
    }

    override fun onItemClicked(media: MediaUiModel) {
        val previousIndex = drawerIndexSelected
        drawerIndexSelected = pickerPager.moveToOf(media)

        binding?.drawerSelector?.setThumbnailSelected(previousIndex, drawerIndexSelected)

        previewAnalytics.clickDrawerThumbnail()
    }

    override fun onDataSetChanged(action: DrawerActionType) {
        when (action) {
            is DrawerActionType.Remove -> {
                val removedIndex = pickerPager.removeData(action.mediaToRemove)
                setUiModelData(action.data)

                if (action.data.isEmpty()) {
                    onBackPickerIntent()
                }

                if(removedIndex == drawerIndexSelected){
                    // move selected item on drawer if selected item is removed
                    drawerIndexSelected = pickerPager.getSelectedIndex()
                    binding?.drawerSelector?.post {
                        binding?.drawerSelector?.setThumbnailSelected(nextIndex = drawerIndexSelected)
                    }
                }
            }
            is DrawerActionType.Add -> {
                setUiModelData(action.data)
            }
            is DrawerActionType.Reorder -> {
                setUiModelData(action.data)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(CACHE_LAST_SELECTION, uiModel)
    }

    private fun restoreDataState(savedInstanceState: Bundle?) {
        // get data from picker
        intent?.let {
            val items = it.getParcelableArrayListExtra<MediaUiModel>(EXTRA_INTENT_PREVIEW)?: listOf()

            if (items.isNotEmpty()) {
                setUiModelData(items)
            } else {
                finish()
            }
        }

        // temporary cache of uiModel
        savedInstanceState?.let {
            it.getParcelableArrayList<MediaUiModel>(CACHE_LAST_SELECTION)
                ?.let { elements ->
                    setUiModelData(elements)
                }
        }
    }

    private fun initView() {
        setupToolbar()

        drawerIndexSelected = uiModel.size - 1

        pickerPager.setupView(uiModel, drawerIndexSelected)
        setupSelectionDrawerOrActionButton()
    }

    private fun setupToolbar() = with(navToolbar) {
        setTitle(getString(R.string.picker_toolbar_preview_title))
        showContinueButtonAs(true)
        onToolbarThemeChanged(ToolbarTheme.Solid)

        if (!param.get().withEditor()) {
            navToolbar.setContinueTitle(getString(R.string.picker_button_upload))
        }
    }

    private fun setupSelectionDrawerOrActionButton() {
        if (param.get().isMultipleSelectionType()) {
            binding?.drawerSelector?.setMaxAdapterSize(uiModel.size)
            binding?.drawerSelector?.addAllData(uiModel)
            binding?.drawerSelector?.show()

            binding?.drawerSelector?.scrollTo(drawerIndexSelected)

            binding?.drawerSelector?.post {
                // set selected index on drawerSelector initialize
                binding?.drawerSelector?.setThumbnailSelected(nextIndex = drawerIndexSelected)
            }
        } else {
            val media = uiModel.first()
            retakeButtonAction(media)
        }
    }

    private fun retakeButtonAction(media: MediaUiModel) {
        binding?.btnRetake?.show()

        val retakeState = if (media.isVideo() && media.isFromPickerCamera) {
            binding?.btnRetake?.videoMode()
            PREVIEW_RETAKE_RECORDER
        } else if (!media.isVideo() && media.isFromPickerCamera) {
            binding?.btnRetake?.photoMode()
            PREVIEW_RETAKE_CAMMERA
        } else {
            binding?.btnRetake?.commonMode()
            PREVIEW_RETAKE_GALLERY
        }

        binding?.btnRetake?.setOnClickListener {
            previewAnalytics.clickRetakeButton(retakeState)
            onCancelOrRetakeMedia(media)
        }
    }

    private fun onCancelOrRetakeMedia(media: MediaUiModel) {
        if (media.isFromPickerCamera) safeFileDelete(media.path)
        cancelIntent()
    }

    private fun setUiModelData(elements: List<MediaUiModel>) {
        uiModel.clear()
        uiModel.addAll(elements)
    }

    private fun cancelIntent() {
        uiModel.clear()
        onBackPickerIntent()
    }

    private fun onBackPickerIntent() {
        val intent = Intent()
        intent.putExtra(RESULT_INTENT_PREVIEW, uiModel)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onFinishIntent(files: ArrayList<String>) {
        val intent = Intent()
        intent.putExtra(RESULT_PICKER, files)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onEditorIntent(files: ArrayList<String>) {
        val intent = Intent()
        intent.putExtra(EXTRA_EDITOR, files)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun initInjector() {
        DaggerPreviewComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .previewModule(PreviewModule())
            .build()
            .inject(this)
    }

    companion object {
        private const val CACHE_LAST_SELECTION = "cache_last_selection"

        const val EXTRA_INTENT_PREVIEW = "extra-intent-preview"
        const val RESULT_INTENT_PREVIEW = "result-intent-preview"
    }

}