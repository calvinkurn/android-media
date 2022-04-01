package com.tokopedia.media.picker.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalyticsImpl
import com.tokopedia.media.picker.analytics.camera.CameraAnalyticsImpl
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.activity.main.component.BottomNavComponent
import com.tokopedia.media.picker.ui.activity.main.component.ParentContainerComponent
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.uimodel.hasVideoBy
import com.tokopedia.media.picker.ui.uimodel.safeRemove
import com.tokopedia.media.picker.utils.delegates.permissionGranted
import com.tokopedia.media.picker.utils.toVideoMaxDurationTextFormat
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity.Companion.EXTRA_INTENT_PREVIEW
import com.tokopedia.picker.common.*
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.types.FragmentType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.toUiModel
import com.tokopedia.picker.common.utils.toMb
import com.tokopedia.picker.common.utils.toSec
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner.cleanUpInternalStorageIfNeeded
import com.tokopedia.utils.image.ImageProcessingUtil
import javax.inject.Inject

/**
 * main applink:
 * tokopedia://media-picker
 * state -> camera, video, gallery, and multiple selection
 *
 * page:
 * camera -> camera page only
 * gallery -> gallery page only
 *
 * mode:
 * image -> image only (camera only and gallery only shown an image)
 * video -> video only (video only and gallery only shown an video)
 *
 * type:
 * single -> single selection
 * multiple -> multiple selection
 *
 * sample use-cases:
 * show camera page and only supported for image:
 * tokopedia://media-picker?page=camera&mode=image
 *
 * show camera page and only supported for video:
 * tokopedia://media-picker?page=camera&mode=video
 *
 * show gallery page and only supported for image:
 * tokopedia://media-picker?page=gallery&mode=image
 *
 * show gallery page and only supported for video:
 * tokopedia://media-picker?page=gallery&mode=video
 *
 * show camera and gallery but only supported for image:
 * tokopedia://media-picker?mode=image
 *
 * show camera and gallery but only supported for video:
 * tokopedia://media-picker?mode=video
 *
 * if you want to set between single or multiple selection, just add this query:
 * ...&type=single/multiple
 */
open class PickerActivity : BaseActivity(), PermissionFragment.Listener,
    NavToolbarComponent.Listener, PickerActivityListener, BottomNavComponent.Listener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var param: ParamCacheManager

    @Inject
    lateinit var galleryAnalytics: GalleryAnalyticsImpl

    @Inject
    lateinit var cameraAnalytics: CameraAnalyticsImpl

    private val hasPermissionGranted: Boolean by permissionGranted()

    protected val medias = arrayListOf<MediaUiModel>()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        initInjector()

        setupParamQueryAndDataIntent()
        restoreDataState(savedInstanceState)

        initView()
        initObservable()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // get data from preview if user had an updated the media elements
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PREVIEW_PAGE && data != null) {
            data.getParcelableArrayListExtra<MediaUiModel>(
                PickerPreviewActivity.RESULT_INTENT_PREVIEW
            )?.toList()?.let {
                stateOnChangePublished(it)
            }

            // exit Picker
            data.getStringArrayListExtra(RESULT_PICKER)?.let {
                onFinishIntent(it)
            }

            // goto Editor
            data.getStringArrayListExtra(EXTRA_EDITOR)?.let {
                onEditorIntent(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventFlowFactory.reset()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            LAST_MEDIA_SELECTION,
            medias
        )
    }

    private fun setupParamQueryAndDataIntent() {
        val pickerParam = PickerIntent.get(intent)

        onPageSourceNotFound(pickerParam)

        // get data from uri query parameter
        intent?.data?.let {
            PickerUiConfig.getStartPageIndex(it)
        }

        // set the picker param as cache
        param.setParam(pickerParam)

        // get pre-included media items
        param.get().includeMedias()
            .map { it.toUiModel() }
            .also {
                stateOnChangePublished(it)
            }
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

    private fun initView() {
        if (hasPermissionGranted) {
            navigateByPageType()
        } else {
            navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
            container.open(FragmentType.PERMISSION)
        }
    }

    private fun initObservable() {
        lifecycleScope.launchWhenResumed {
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
                    medias.isNotEmpty()
                )
            }
        }
    }

    private fun navigateByPageType() {
        when (param.get().pageType()) {
            PageType.CAMERA -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
                container.open(FragmentType.CAMERA)
            }
            PageType.GALLERY -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
                container.open(FragmentType.GALLERY)
            }
            else -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)

                // display the tab navigation
                bottomNavTab.setupView()

                // start position of tab
                bottomNavTab.onStartPositionChanged(
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

    private fun onFinishIntent(path: ArrayList<String>) {
        val intent = Intent()
        intent.putExtra(RESULT_PICKER, path)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onEditorIntent(path: ArrayList<String>) {
        // TODO
    }

    override fun onPermissionGranted() {
        navigateByPageType()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        container.cameraFragment()?.gestureDetector?.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onCloseClicked() {
        if (container.isFragmentActive(FragmentType.GALLERY)) {
            galleryAnalytics.clickCloseButton()
        }
        finish()
    }

    override fun onContinueClicked() {
        if (container.isFragmentActive(FragmentType.GALLERY)) {
            galleryAnalytics.clickNextButton()
        }

        val intent = Intent(this, PickerPreviewActivity::class.java).apply {
            putExtra(EXTRA_INTENT_PREVIEW, ArrayList(medias))
        }

        startActivityForResult(intent, REQUEST_PREVIEW_PAGE)
    }

    override fun onCameraThumbnailClicked() {
        onContinueClicked()
    }

    override fun onCameraTabSelected(isDirectClick: Boolean) {
        container.open(FragmentType.CAMERA)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        container.resetBottomNavMargin()
        if (isDirectClick) galleryAnalytics.clickCameraTab()
    }

    override fun onGalleryTabSelected(isDirectClick: Boolean) {
        container.open(FragmentType.GALLERY)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
        container.addBottomNavMargin()
        if (isDirectClick) cameraAnalytics.clickGalleryTab()
    }

    override fun tabVisibility(isShown: Boolean) {
        if (!param.get().isCommonPageType()) return

        bottomNavTab.container().showWithCondition(isShown)
    }

    override fun navigateToCameraPage() {
        bottomNavTab.navigateToCameraTab()
    }

    override fun mediaSelected(): List<MediaUiModel> {
        return medias
    }

    override fun hasVideoLimitReached(): Boolean {
        return medias.hasVideoBy(param.get().maxVideoCount())
    }

    override fun hasMediaLimitReached(): Boolean {
        return medias.size == param.get().maxMediaTotal()
    }

    override fun isMinVideoDuration(model: MediaUiModel): Boolean {
        return model.videoDuration(applicationContext) <= param.get().minVideoDuration()
    }

    override fun isMaxVideoDuration(model: MediaUiModel): Boolean {
        return model.videoDuration(applicationContext) > param.get().maxVideoDuration()
    }

    override fun isMaxVideoSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.get().maxVideoFileSize())
    }

    override fun isMinImageResolution(model: MediaUiModel): Boolean {
        return model.isMinImageRes(param.get().minImageResolution())
    }

    override fun isMaxImageResolution(model: MediaUiModel): Boolean {
        return model.isMaxImageRes(param.get().maxImageResolution())
    }

    override fun isMaxImageSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.get().maxImageFileSize())
    }

    override fun isMinStorageThreshold(): Boolean {
        return viewModel.isDeviceStorageFull()
    }

    override fun onShowMediaLimitReachedGalleryToast() {
        onShowValidationToaster(
            R.string.picker_selection_limit_message,
            param.get().maxMediaTotal()
        )

        galleryAnalytics.galleryMaxPhotoLimit()
    }

    override fun onShowVideoLimitReachedGalleryToast() {
        onShowValidationToaster(
            R.string.picker_selection_limit_video,
            param.get().maxVideoCount()
        )

        galleryAnalytics.galleryMaxVideoLimit()
    }

    override fun onShowMediaLimitReachedCameraToast() {
        onShowValidationToaster(
            R.string.picker_capture_limit_photo,
            param.get().maxMediaTotal()
        )
        cameraAnalytics.maxPhotoLimit()
    }

    override fun onShowVideoLimitReachedCameraToast() {
        onShowValidationToaster(
            R.string.picker_capture_limit_video,
            param.get().maxVideoCount()
        )
        cameraAnalytics.maxVideoLimit()
    }

    override fun onShowVideoMinDurationToast() {
        onShowValidationToaster(
            R.string.picker_video_duration_min_limit,
            param.get().minVideoDuration().toSec()
        )

        galleryAnalytics.minVideoDuration()
    }

    override fun onShowVideoMaxDurationToast() {
        onShowValidationToaster(
            R.string.picker_video_duration_max_limit,
            param.get().maxVideoDuration().toSec().toVideoMaxDurationTextFormat(this)
        )

        galleryAnalytics.maxVideoDuration()
    }

    override fun onShowVideoMaxFileSizeToast() {
        onShowValidationToaster(
            R.string.picker_video_max_size,
            param.get().maxVideoFileSize().toMb()
        )

        galleryAnalytics.maxVideoSize()
    }

    override fun onShowImageMaxFileSizeToast() {
        onShowValidationToaster(
            R.string.picker_image_max_size,
            param.get().maxImageFileSize().toMb()
        )

        galleryAnalytics.maxImageSize()
    }

    override fun onShowImageMinResToast() {
        onShowValidationToaster(
            R.string.picker_image_res_min_limit,
            param.get().minImageResolution()
        )

        galleryAnalytics.minImageResolution()
    }

    override fun onShowImageMaxResToast() {
        onShowValidationToaster(
            R.string.picker_image_res_max_limit,
            param.get().maxImageResolution()
        )
    }

    override fun onShowMinStorageThresholdToast() {
        onShowValidationToaster(
            R.string.picker_storage_threshold_message,
            param.get().minStorageThreshold().toMb()
        )
    }

    override fun onShowFailToVideoRecordToast() {
        onShowToaster(
            getString(R.string.picker_storage_fail_video_record),
            Toaster.TYPE_ERROR
        )
        cameraAnalytics.recordLowStorage()
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
        return PickerFragmentFactoryImpl()
    }

    protected open fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .pickerModule(PickerModule())
            .build()
            .inject(this)
    }

    companion object {
        const val REQUEST_PREVIEW_PAGE = 123

        private const val LAST_MEDIA_SELECTION = "last_media_selection"
    }

}