package com.tokopedia.media.preview.ui.activity

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ActivityPreviewBinding
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.utils.goToSettings
import com.tokopedia.media.picker.utils.parcelableArrayListExtra
import com.tokopedia.media.picker.utils.permission.PermissionManager
import com.tokopedia.media.picker.utils.permission.PermissionRequestCallback
import com.tokopedia.media.picker.utils.permission.isGranted
import com.tokopedia.media.preview.analytics.*
import com.tokopedia.media.preview.di.DaggerPreviewComponent
import com.tokopedia.media.preview.ui.component.PreviewPagerComponent
import com.tokopedia.picker.common.*
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

open class PickerPreviewActivity : BaseActivity(), NavToolbarComponent.Listener,
    DrawerSelectionWidget.Listener {

    @Inject
    lateinit var param: PickerCacheManager

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var previewAnalytics: PreviewAnalytics

    private val binding: ActivityPreviewBinding? by viewBinding()
    private val uiModel = arrayListOf<MediaUiModel>()

    // variable for control index item to be rendered first
    private var drawerIndexSelected = 0

    private val loaderDialog by lazy {
        LoaderDialog(this)
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

    private val permissionManager: PermissionManager by lazy {
        PermissionManager.init(
            this,
            object : PermissionRequestCallback {
            override fun onDenied(permissions: List<String>) {}

            override fun onPermissionPermanentlyDenied(permissions: List<String>) {
                if (permissions.isNotEmpty()) {
                    startActivity(goToSettings())
                }
            }

            override fun onGranted(permissions: List<String>) {
                viewModel.files(uiModel)
            }
        })
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[PreviewViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        restoreDataState(savedInstanceState)
        initInjector()
        initObservable()
        initView()
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
        pickerPager.pauseVideoPlayer()
    }

    override fun onCloseClicked() {
        previewAnalytics.clickBackButton()
        onBackPickerIntent()
    }

    override fun onBackPressed() {
        if (param.get().isMultipleSelectionType()) {
            onBackPickerIntent()
        } else {
            if (uiModel.isEmpty()) {
                cancelIntent()
                return
            }

            onCancelOrRetakeMedia(uiModel.first())
        }
    }

    override fun onDrawerItemClicked(media: MediaUiModel) {
        val previousIndex = drawerIndexSelected
        drawerIndexSelected = pickerPager.moveToOf(media)

        binding?.drawerSelector?.setThumbnailSelected(previousIndex, drawerIndexSelected)

        previewAnalytics.clickDrawerThumbnail()
    }

    override fun onDrawerDataSetChanged(action: DrawerActionType) {
        when (action) {
            is DrawerActionType.Remove -> {
                val removedIndex = pickerPager.removeData(action.mediaToRemove)
                setUiModelData(action.data)

                if (action.data.isEmpty()) {
                    onBackPickerIntent()
                }

                if (removedIndex == drawerIndexSelected) {
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

    override fun onContinueClicked() {
        checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissionManager.onRequestPermissionsResult(permissions, grantResults)
        }
    }

    private fun checkPermission() {
        if (isGranted(this, WRITE_EXTERNAL_STORAGE) && SDK_INT <= VERSION_CODES.P) {
            permissionManager.requestPermission(WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE)
        } else {
            viewModel.files(uiModel)
        }
    }

    private fun initObservable() {
        lifecycleScope.launchWhenResumed {
            viewModel.result
                .distinctUntilChanged()
                .filter { it.originalPaths.isNotEmpty() }
                .collectLatest {
                    val withEditor = param.get().isEditorEnabled() || param.get().isImmersiveEditorEnabled()

                    val buttonState = if (withEditor) {
                        PREVIEW_PAGE_LANJUT
                    } else {
                        PREVIEW_PAGE_UPLOAD
                    }

                    previewAnalytics.clickNextButton(buttonState)
                    onFinishIntent(it, withEditor)
                }
        }

        viewModel.isLoading.observe(this) {
            onShowDialog(it)
        }
    }

    private fun onShowDialog(isShown: Boolean) {
        if (!isShown && loaderDialog.dialog.isShowing) {
            loaderDialog.dialog.dismiss()
            return
        }

        loaderDialog.dialog.setOverlayClose(false)
        loaderDialog.setLoadingText("")
        loaderDialog.show()
    }

    private fun restoreDataState(savedInstanceState: Bundle?) {
        // get data from picker
        intent?.let {
            val items = it.parcelableArrayListExtra<MediaUiModel>(EXTRA_INTENT_PREVIEW) ?: listOf()

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

        param.get().apply {
            if (!isEditorEnabled() || !isImmersiveEditorEnabled()) {
                navToolbar.setContinueTitle(
                    if (previewActionText().isNotEmpty()) {
                        previewActionText()
                    } else {
                        getString(R.string.picker_button_upload)
                    }
                )
            }
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
            if (uiModel.isEmpty()) {
                cancelIntent()
                return
            }

            val media = uiModel.first()
            retakeButtonAction(media)
        }
    }

    private fun retakeButtonAction(media: MediaUiModel) {
        binding?.btnRetake?.show()
        binding?.btnRetake?.setModeUi(media)

        val isVideoFromCamera = media.file?.isVideo() == true && media.isCacheFile
        val isImageFromCamera = media.file?.isImage() == true && media.isCacheFile

        val retakeState = when {
            isVideoFromCamera -> PREVIEW_RETAKE_RECORDER
            isImageFromCamera -> PREVIEW_RETAKE_CAMMERA
            else -> PREVIEW_RETAKE_GALLERY
        }

        binding?.btnRetake?.setOnClickListener {
            previewAnalytics.clickRetakeButton(retakeState)
            onCancelOrRetakeMedia(media)
        }
    }

    private fun onCancelOrRetakeMedia(media: MediaUiModel) {
        if (media.isCacheFile) {
            media.file?.safeDelete()
        }

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

    private fun onFinishIntent(files: PickerResult, withEditor: Boolean) {
        val intent = Intent()
        intent.putExtra(EXTRA_RESULT_PICKER, files)
        intent.putExtra(EXTRA_EDITOR_PICKER, withEditor)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    protected open fun initInjector() {
        DaggerPreviewComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    companion object {
        private const val CACHE_LAST_SELECTION = "cache_last_selection"
        private const val PERMISSION_REQUEST_CODE = 135

        fun start(activity: ComponentActivity, medias: ArrayList<MediaUiModel>, reqCode: Int) {
            activity.startActivityForResult(
                Intent(activity.applicationContext, PickerPreviewActivity::class.java).apply {
                    putExtra(EXTRA_INTENT_PREVIEW, medias)
                },
                reqCode
            )
        }
    }

}
