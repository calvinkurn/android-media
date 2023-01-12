package com.tokopedia.media.picker.ui.activity.picker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.di.PickerInjector
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.component.BottomNavComponent
import com.tokopedia.media.picker.ui.component.ParentContainerComponent
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.media.picker.utils.permission.hasPermissionRequiredGranted
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.picker.common.*
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.types.FragmentType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.safeRemove
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.toUiModel
import com.tokopedia.picker.common.utils.VideoDurationRetriever
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner.cleanUpInternalStorageIfNeeded
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

open class PickerActivity : BaseActivity(), PermissionFragment.Listener,
    NavToolbarComponent.Listener, PickerActivityContract, BottomNavComponent.Listener {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var param: PickerCacheManager

    @Inject
    lateinit var pickerAnalytics: PickerAnalytics

    protected val medias = arrayListOf<MediaUiModel>()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[PickerViewModel::class.java]
    }

    private val navToolbar by uiComponent {
        NavToolbarComponent(
            parent = it,
            listener = this,
            useArrowIcon = false
        )
    }

    private val container by uiComponent {
        ParentContainerComponent(
            parent = it,
            fragmentManager = supportFragmentManager,
            fragmentFactory = createFragmentFactory()
        )
    }

    private val bottomNavTab by uiComponent {
        BottomNavComponent(
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
    }

    override fun onStart() {
        super.onStart()
        setupParam()
    }

    override fun onPause() {
        super.onPause()
        EventFlowFactory.dispose()
    }

    override fun onStop() {
        super.onStop()
        EventFlowFactory.reset()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // get data from preview if user had an updated the media elements
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PREVIEW_PAGE && data != null) {
            data.getParcelableArrayListExtra<MediaUiModel>(RESULT_INTENT_PREVIEW)?.toList()?.let {
                stateOnChangePublished(it)
            }

            // exit picker
            data.getParcelableExtra<PickerResult>(EXTRA_RESULT_PICKER)?.let {
                onRemoveSubSourceMedia()

                val withEditor = data.getBooleanExtra(EXTRA_EDITOR_PICKER, false)

                if (withEditor) {
                    onEditorIntent(it)
                } else {
                    onFinishIntent(it)
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_EDITOR_PAGE && data != null) {
            data.getParcelableExtra<EditorResult>(RESULT_INTENT_EDITOR)?.let {
                onFinishIntent(
                    PickerResult(it.originalPaths, editedImages = it.editedImages)
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

    private fun setupParam() {
        val mParam = intent?.getParcelableExtra<PickerParam>(EXTRA_PICKER_PARAM)

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
                    stateOnChangePublished(elements)
                }
        }
    }

    private fun initView(param: PickerParam) {
        // get pre-included media items
        viewModel.preSelectedMedias(param)

        // get data from uri query parameter
        intent?.data?.let {
            PickerUiConfig.getStartPageIndex(it)
        }

        if (isRootPermissionGranted()) {
            onPageViewByType()
        } else {
            onPermissionPageView()
        }
    }

    private fun initObservable() {
        viewModel.pickerParam.observe(this) {
            onPageSourceNotFound(it)
            initView(it)
        }

        viewModel.editorParam.observe(this) {
            val (result, param) = it

            val intent = MediaEditor.intent(this@PickerActivity, result.originalPaths, param)
            startActivityForResult(intent, REQUEST_EDITOR_PAGE)
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

            val fileToUiModel = files.mapNotNull {
                val mPickerFile = it?.asPickerFile()
                mPickerFile?.toUiModel()
            }

            stateOnChangePublished(fileToUiModel)
        }
    }

    private fun onPageViewByType() {
        when (param.get().pageType()) {
            // single page -> camera
            PageType.CAMERA -> onCameraPageView()
            // single page -> gallery
            PageType.GALLERY -> onGalleryPageView()
            // multiple page -> by index
            else -> {
                bottomNavTab.setupView()

                bottomNavTab.navigateToIndexOf(
                    PickerUiConfig.startPageIndex
                )
            }
        }
    }

    private fun onPageSourceNotFound(param: PickerParam) {
        if (GlobalConfig.isAllowDebuggingTools()) return

        if (param.pageSourceName().isEmpty()) {
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
            stateOnRemovePublished(medias.last())
        }
    }

    private fun onFinishIntent(data: PickerResult) {
        val intent = Intent()
        intent.putExtra(EXTRA_RESULT_PICKER, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onEditorIntent(data: PickerResult) {
        viewModel.navigateToEditorPage(data)
    }

    private fun onPermissionPageView() {
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
        container.open(FragmentType.PERMISSION)
    }

    private fun onCameraPageView() {
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        container.open(FragmentType.CAMERA)
    }

    private fun onGalleryPageView() {
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
        container.open(FragmentType.GALLERY)
    }

    override fun onPermissionGranted() {
        onPageViewByType()
    }

    override fun isRootPermissionGranted(): Boolean {
        val page = param.get().pageType()
        val mode = param.get().modeType()

        return hasPermissionRequiredGranted(this, page, mode)
    }

    override fun onGetVideoDuration(media: MediaUiModel): Int {
        return VideoDurationRetriever.get(applicationContext, media.file)
    }

    override fun onCameraTabSelected(isDirectClick: Boolean) {
        container.resetBottomNavMargin()
        onCameraPageView()

        if (isDirectClick) {
            pickerAnalytics.clickCameraTab()
        }
    }

    override fun onGalleryTabSelected(isDirectClick: Boolean) {
        container.addBottomNavMargin()
        onGalleryPageView()

        if (isDirectClick) {
            pickerAnalytics.clickGalleryTab()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!param.get().isIncludeVideoFile()) {
            return super.dispatchTouchEvent(ev)
        }

        container.cameraFragment()?.run {
            val cameraFragment = this

            if (cameraFragment.isAdded && cameraFragment.view != null) {
                cameraFragment.gestureDetector.onTouchEvent(ev)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onCloseClicked() {
        if (container.isFragmentActive(FragmentType.GALLERY)) {
            pickerAnalytics.clickCloseButton()
        }

        finish()
    }

    override fun onContinueClicked() {
        if (container.isFragmentActive(FragmentType.GALLERY)) {
            pickerAnalytics.clickNextButton()
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
        container.container().let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, type).show()
        }
    }

    protected open fun createFragmentFactory(): PickerFragmentFactory {
        return PickerFragmentFactoryImpl(
            fragmentManager = supportFragmentManager,
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
        private const val MILLIS_TO_SEC = 1000
    }

}
