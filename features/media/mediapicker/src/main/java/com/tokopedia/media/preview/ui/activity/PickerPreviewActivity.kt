package com.tokopedia.media.preview.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ActivityPreviewBinding
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
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
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File
import javax.inject.Inject

class PickerPreviewActivity : BaseActivity()
    , NavToolbarComponent.Listener
    , DrawerSelectionWidget.Listener {

    @Inject lateinit var cacheManager: ParamCacheManager

    private val binding: ActivityPreviewBinding? by viewBinding()
    private val uiModel = arrayListOf<MediaUiModel>()

    private val param by lazy {
        cacheManager.getParam()
    }

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
        onBackPickerIntent()
    }

    override fun onBackPressed() {
        onBackPickerIntent()
    }

    override fun onContinueClicked() {
        uiModel.map { it.path }.let {
            val result = ArrayList(it)

            if (param.withEditor()) {
                onEditorIntent(result)
            } else {
                onFinishIntent(result)
            }
        }
    }

    override fun onItemClicked(media: MediaUiModel) {
        pickerPager.moveToOf(media)
    }

    override fun onDataSetChanged(action: DrawerActionType) {
        when (action) {
            is DrawerActionType.Remove -> {
                pickerPager.removeData(action.mediaToRemove)
                setUiModelData(action.data)

                if (action.data.isEmpty()) {
                    onBackPickerIntent()
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
        pickerPager.setupView(uiModel)
        setupSelectionDrawerOrActionButton()
    }

    private fun setupToolbar() = with(navToolbar) {
        setTitle(getString(R.string.picker_toolbar_preview_title))
        showContinueButtonAs(true)
        onToolbarThemeChanged(ToolbarTheme.Solid)
        if (param.withEditor()) {
            navToolbar.setContinueTitle(getString(R.string.picker_toolbar_upload))
        }
    }

    private fun setupSelectionDrawerOrActionButton() {
        if (cacheManager.getParam().isMultipleSelectionType()) {
            binding?.drawerSelector?.setMaxAdapterSize(uiModel.size)
            binding?.drawerSelector?.addAllData(uiModel)
            binding?.drawerSelector?.show()
        } else {
            val media = uiModel.first()
            retakeButtonAction(media)
        }
    }

    private fun retakeButtonAction(media: MediaUiModel) {
        binding?.btnRetake?.show()

        if (media.isVideo() && media.isFromPickerCamera) {
            binding?.btnRetake?.videoMode()
        } else if (!media.isVideo() && media.isFromPickerCamera) {
            binding?.btnRetake?.photoMode()
        } else {
            binding?.btnRetake?.commonMode()
        }

        binding?.btnRetake?.setOnClickListener {
            deleteLocalCameraMedia(media)
            cancelIntent()
        }
    }

    private fun deleteLocalCameraMedia(media: MediaUiModel) {
        if (media.isFromPickerCamera) {
            val file = File(media.path)

            if (file.exists()) {
                file.delete()
            }
        }
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