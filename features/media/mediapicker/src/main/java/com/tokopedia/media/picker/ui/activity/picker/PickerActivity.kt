package com.tokopedia.media.picker.ui.activity.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.picker.analytics.LogType
import com.tokopedia.media.picker.analytics.Logger
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.di.PickerInjector
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.component.BottomNavUiComponent
import com.tokopedia.media.picker.ui.component.PagerContainerUiComponent
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.media.picker.ui.publisher.PickerEventBus
import com.tokopedia.media.picker.ui.publisher.observe
import com.tokopedia.media.picker.ui.widget.LoaderDialogWidget
import com.tokopedia.media.picker.utils.parcelableArrayListExtra
import com.tokopedia.media.picker.utils.parcelableExtra
import com.tokopedia.media.picker.utils.permission.hasPermissionRequiredGranted
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.picker.common.*
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.safeRemove
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner.cleanUpInternalStorageIfNeeded
import com.tokopedia.utils.image.ImageProcessingUtil
import javax.inject.Inject

open class PickerActivity : BaseActivity(), PermissionFragment.Listener,
    NavToolbarComponent.Listener, PickerActivityContract, BottomNavUiComponent.Listener {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var param: PickerCacheManager

    @Inject
    lateinit var pickerAnalytics: PickerAnalytics

    @Inject
    lateinit var eventBus: PickerEventBus

    protected val medias = arrayListOf<MediaUiModel>()

    private val startTimeInMillis = System.currentTimeMillis()
    private var loaderDialog: LoaderDialogWidget? = null
    private var isOnVideoRecording = false

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[PickerViewModel::class.java]
    }

    private val pagerContainer by uiComponent {
        PagerContainerUiComponent(
            parent = it,
            activity = this,
            param = param,
            factory = createFragmentFactory()
        )
    }

    private val navToolbar by uiComponent {
        NavToolbarComponent(
            parent = it,
            listener = this,
            useArrowIcon = false
        )
    }

    private val bottomNavTab by uiComponent {
        BottomNavUiComponent(
            parent = it,
            listener = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        restoreDataState(savedInstanceState)

        initObservable()
        setupParam()
    }

    override fun onResume() {
        super.onResume()
        resetVideoRecordingState()
    }

    override fun onBackPressed() {
        if (isOnVideoRecording) return
        super.onBackPressed()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)

        if (isSplitInstallEnabled()) {
            SplitCompat.installActivity(this)
        }
    }

    override fun onPause() {
        super.onPause()
        eventBus.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        eventBus.reset()
    }

    private val immersiveEditorIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data?.getStringExtra(RESULT_UNIVERSAL_EDITOR) ?: return@registerForActivityResult
            val result = PickerResult(listOf(data))

            onFinishIntent(result)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // get data from preview if user had an updated the media elements
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PREVIEW_PAGE && data != null) {
            data.parcelableArrayListExtra<MediaUiModel>(RESULT_INTENT_PREVIEW)
                ?.toList()
                ?.let {
                    eventBus.notifyDataOnChangedEvent(it)
                }

            // exit picker
            data.parcelableExtra<PickerResult>(EXTRA_RESULT_PICKER)?.let {
                onRemoveSubSourceMedia()

                val withEditor = data.getBooleanExtra(EXTRA_EDITOR_PICKER, false)

                if (withEditor) {
                    onEditorIntent(it)
                } else {
                    onFinishIntent(it)
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_EDITOR_PAGE && data != null) {
            data.parcelableExtra<EditorResult>(RESULT_INTENT_EDITOR)?.let {
                val selectedIncludeMedia = viewModel.editorParam.value?.first?.selectedIncludeMedia ?: emptyList()
                onFinishIntent(
                    PickerResult(it.originalPaths, editedImages = it.editedImages, selectedIncludeMedia = selectedIncludeMedia)
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            LAST_MEDIA_SELECTION,
            medias
        )
    }

    private fun isSplitInstallEnabled(): Boolean {
        return true
    }

    private fun resetVideoRecordingState() {
        isOnVideoRecording = false
        viewModel.isOnVideoRecording(isOnVideoRecording)
    }

    private fun setupParam() {
        val mParam = intent?.parcelableExtra<PickerParam>(EXTRA_PICKER_PARAM)

        if (mParam?.pageSourceName()?.isNotEmpty() == true && mParam.subPageSourceName().isEmpty()) {
            param.disposeSubPicker()
        }

        viewModel.setPickerParam(mParam)
    }

    private fun restoreDataState(savedInstanceState: Bundle?) {
        cleanUpInternalStorageIfNeeded(this, ImageProcessingUtil.DEFAULT_DIRECTORY)

        savedInstanceState?.let {
            // restore the last media selection to the drawer
            it.getParcelableArrayList<MediaUiModel>(LAST_MEDIA_SELECTION)
                ?.let { elements ->
                    eventBus.notifyDataOnChangedEvent(elements)
                }
        }
    }

    private fun renderPageByType() {
        when (param.get().pageType()) {
            PageType.CAMERA -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
                pagerContainer.setupCameraPage()
            }
            PageType.GALLERY -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
                pagerContainer.setupGalleryPage()
            }
            else -> {
                pagerContainer.setupCommonPage()
                bottomNavTab.setupView()

                bottomNavTab.navigateToIndexOf(
                    PickerUiConfig.startPageIndex
                )
            }
        }
    }

    private fun renderPermissionPage() {
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
        pagerContainer.setupPermissionPage()
    }

    private fun navigateToCameraPage() {
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        pagerContainer.navigateToCameraPage()
    }

    private fun navigateToGalleryPage() {
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
        pagerContainer.navigateToGalleryPage()
    }

    private fun initView(param: PickerParam) {
        // get pre-included media items
        viewModel.preSelectedMedias(param)

        // get data from uri query parameter
        intent?.data?.let {
            PickerUiConfig.getStartPageIndex(it)
        }

        if (isRootPermissionGranted()) {
            renderPageByType()
        } else {
            renderPermissionPage()
        }
    }

    private fun initObservable() {
        viewModel.pickerParam.observe(this) {
            onPageSourceNotFound(it.pageSourceName())
            initView(it)
        }

        viewModel.editorParam.observe(this) {
            val (result, param) = it

            val intent = MediaEditor.intent(this@PickerActivity, result.originalPaths, param)
            startActivityForResult(intent, REQUEST_EDITOR_PAGE)
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                loaderDialog = LoaderDialogWidget(this)
                loaderDialog?.show()
            } else {
                loaderDialog?.dismiss()
                Logger.send(
                    startTime = startTimeInMillis,
                    endTime = System.currentTimeMillis()
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.observe(
                onChanged = {
                    medias.clear()
                    medias.addAll(it)
                },
                onRemoved = {
                    if (medias.contains(it)) {
                        medias.safeRemove(it)
                    }
                },
                onAdded = {
                    if (!param.get().isMultipleSelectionType()) medias.clear()
                    if (!medias.contains(it)) medias.add(it)

                    if (!param.get().isMultipleSelectionType()) {
                        onContinueClicked()
                    }
                }
            ) {
                navToolbar.showContinueButtonAs(
                    medias.isNotEmpty() && param.get().isMultipleSelectionType()
                )
            }
        }

        viewModel.includeMedias.observe(this) { files ->
            if (files.isEmpty()) return@observe

            files.filterNotNull()
                .forEach {
                    eventBus.addMediaEvent(it)
                }
        }

        viewModel.connectionIssue.observe(this) { message ->
            onShowToaster(message, Toaster.TYPE_ERROR)
            Logger.send(LogType.NoInternetConnection)

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, TOAST_DELAYED)
        }

        viewModel.isOnVideoRecording.observe(this) { isRecord ->
            navToolbar.setVisibility(isRecord.not())
            isOnVideoRecording = isRecord
        }
    }

    private fun onPageSourceNotFound(sourceName: String) {
        if (GlobalConfig.isAllowDebuggingTools()) return

        if (sourceName.isEmpty()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.picker_page_source_not_found),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    private fun onRemoveSubSourceMedia() {
        if (param.get().subPageSourceName().isNotEmpty() && medias.isNotEmpty()) {
            eventBus.removeMediaEvent(medias.last())
        }
    }

    private fun onFinishIntent(data: PickerResult) {
        val intent = Intent()
        intent.putExtra(EXTRA_RESULT_PICKER, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onEditorIntent(data: PickerResult) {
        if (param.get().isImmersiveEditorEnabled()) {
            // immersive editor
            val intent = UniversalEditor.intent(this) {
                setPageSource(param.get().pageSource())
                filePaths(data.originalPaths)
            }

            immersiveEditorIntent.launch(intent)
        } else {
            // old editor
            viewModel.navigateToEditorPage(data)
        }
    }

    override fun onPermissionGranted() {
        renderPageByType()
    }

    override fun isRootPermissionGranted(): Boolean {
        val page = param.get().pageType()
        val mode = param.get().modeType()

        return hasPermissionRequiredGranted(this, page, mode)
    }

    override fun onGetVideoDuration(media: MediaUiModel): Int {
        return media.duration
    }

    override fun onCameraTabSelected(isDirectClick: Boolean) {
        navigateToCameraPage()

        if (isDirectClick) {
            pickerAnalytics.clickCameraTab()
        }
    }

    override fun onGalleryTabSelected(isDirectClick: Boolean) {
        navigateToGalleryPage()

        if (isDirectClick) {
            pickerAnalytics.clickGalleryTab()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!param.get().isIncludeVideoFile()) {
            return super.dispatchTouchEvent(ev)
        }

        pagerContainer.cameraFragment()?.let {
            if (it.isAdded && it.view != null) {
                it.gestureDetector.onTouchEvent(ev)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCloseClicked() {
        if (pagerContainer.isGalleryFragmentActive()) {
            pickerAnalytics.clickCloseButton()
        }

        finish()
    }

    override fun onContinueClicked() {
        if (pagerContainer.isGalleryFragmentActive()) {
            pickerAnalytics.clickCloseButton()
        }

        PickerPreviewActivity.start(this, ArrayList(medias), REQUEST_PREVIEW_PAGE)
    }

    override fun onCameraThumbnailClicked() {
        onContinueClicked()
    }

    override fun parentTabIsShownAs(isShown: Boolean) {
        if (!param.get().isCommonPageType()) return

        bottomNavTab.container().showWithCondition(isShown)
    }

    override fun onEmptyStateActionClicked() {
        bottomNavTab.navigateToCameraTab()
    }

    override fun mediaSelected(): List<MediaUiModel> {
        return medias
    }

    override fun hasVideoLimitReached(): Boolean {
        val videoFileSize = medias.filter { it.file?.isVideo() == true }.size
        return videoFileSize >= param.get().maxVideoCount()
    }

    override fun hasMediaLimitReached(): Boolean {
        return medias.size == param.get().maxMediaTotal()
    }

    override fun isMinVideoDuration(model: MediaUiModel): Boolean {
        return onGetVideoDuration(model) <= param.get().minVideoDuration()
    }

    override fun isMaxVideoDuration(model: MediaUiModel): Boolean {
        return onGetVideoDuration(model) > param.get().maxVideoDuration()
    }

    override fun isMaxVideoSize(model: MediaUiModel): Boolean {
        return model.file?.isSizeMoreThan(param.get().maxVideoFileSize()) == true
    }

    override fun isMinImageResolution(model: MediaUiModel): Boolean {
        return model.file?.isMinImageRes(param.get().minImageResolution()) == true
    }

    override fun isMaxImageResolution(model: MediaUiModel): Boolean {
        return model.file?.isMaxImageRes(param.get().maxImageResolution()) == true
    }

    override fun isMaxImageSize(model: MediaUiModel): Boolean {
        return model.file?.isSizeMoreThan(param.get().maxImageFileSize()) == true
    }

    override fun isMinStorageThreshold() = viewModel.isDeviceStorageAlmostFull()

    override fun onShowMediaLimitReachedGalleryToast() {
        onShowValidationToaster(
            R.string.picker_selection_limit_message,
            param.get().maxMediaTotal()
        )

        pickerAnalytics.galleryMaxPhotoLimit()
    }

    override fun onShowVideoLimitReachedGalleryToast() {
        onShowValidationToaster(
            R.string.picker_selection_limit_video,
            param.get().maxVideoCount()
        )

        pickerAnalytics.galleryMaxVideoLimit()
    }

    override fun onShowMediaLimitReachedCameraToast() {
        onShowValidationToaster(
            R.string.picker_capture_limit_photo,
            param.get().maxMediaTotal()
        )

        pickerAnalytics.maxPhotoLimit()
    }

    override fun onShowVideoLimitReachedCameraToast() {
        onShowValidationToaster(
            R.string.picker_capture_limit_video,
            param.get().maxVideoCount()
        )

        pickerAnalytics.maxVideoLimit()
    }

    override fun onShowVideoMinDurationToast() {
        val durationInSec = param.get().minVideoDuration() / MILLIS_TO_SEC

        onShowValidationToaster(
            R.string.picker_video_duration_min_limit,
            durationInSec
        )

        pickerAnalytics.minVideoDuration()
    }

    override fun onShowVideoMaxDurationToast() {
        val durationInSec = param.get().maxVideoDuration() / MILLIS_TO_SEC

        onShowValidationToaster(
            R.string.picker_video_duration_max_limit,
            durationInSec.humanize(this)
        )

        pickerAnalytics.maxVideoDuration()
    }

    override fun onShowVideoMaxFileSizeToast() {
        val sizeInMb = param.get().maxVideoFileSize() / BYTES_TO_MB

        onShowValidationToaster(
            R.string.picker_video_max_size,
            sizeInMb
        )

        pickerAnalytics.maxVideoSize()
    }

    override fun onShowImageMaxFileSizeToast() {
        val sizeInMb = param.get().maxImageFileSize() / BYTES_TO_MB

        onShowValidationToaster(
            R.string.picker_image_max_size,
            sizeInMb
        )

        pickerAnalytics.maxImageSize()
    }

    override fun onShowImageMinResToast() {
        onShowValidationToaster(
            R.string.picker_image_res_min_limit,
            param.get().minImageResolution()
        )

        pickerAnalytics.minImageResolution()
    }

    override fun onShowImageMaxResToast() {
        onShowValidationToaster(
            R.string.picker_image_res_max_limit,
            param.get().maxImageResolution()
        )
    }

    override fun onShowFailToVideoRecordToast() {
        onShowToaster(
            getString(R.string.picker_storage_fail_video_record),
            Toaster.TYPE_ERROR
        )

        pickerAnalytics.recordLowStorage()
    }

    private fun onShowValidationToaster(messageId: Int, param: Any) {
        val content = getString(messageId, param)
        onShowToaster(content)
    }

    private fun onShowToaster(message: String, type: Int = Toaster.TYPE_NORMAL) {
        pagerContainer.container().let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, type).show()
        }
    }

    protected open fun createFragmentFactory(): PickerFragmentFactory {
        return PickerFragmentFactoryImpl(
            mFragmentManager = supportFragmentManager,
            classLoader = applicationContext.classLoader
        )
    }

    protected open fun initInjector() {
        PickerInjector
            .get(this)
            .inject(this)
    }

    companion object {
        const val REQUEST_PREVIEW_PAGE = 123
        const val REQUEST_EDITOR_PAGE = 456

        private const val LAST_MEDIA_SELECTION = "last_media_selection"

        private const val BYTES_TO_MB = 1000000
        private const val TOAST_DELAYED = 3000L
        private const val MILLIS_TO_SEC = 1000
    }

}
