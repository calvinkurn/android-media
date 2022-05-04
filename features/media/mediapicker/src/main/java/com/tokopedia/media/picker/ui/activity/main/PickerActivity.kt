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
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.di.DaggerPickerComponent
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

open class PickerActivity : BaseActivity()
    , PermissionFragment.Listener
    , NavToolbarComponent.Listener
    , PickerActivityListener
    , BottomNavComponent.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var param: ParamCacheManager
    @Inject lateinit var pickerAnalytics: PickerAnalytics

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
            data.getParcelableArrayListExtra<MediaUiModel>(RESULT_INTENT_PREVIEW)?.toList()?.let {
                stateOnChangePublished(it)
            }

            // exit picker
            data.getParcelableExtra<PickerResult>(EXTRA_RESULT_PICKER)?.let {
                val withEditor = data.getBooleanExtra(EXTRA_EDITOR_PICKER, false)

                if (withEditor) {
                    onEditorIntent(it)
                } else {
                    onFinishIntent(it)
                }
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
        val pickerParam = intent?.getParcelableExtra(EXTRA_PICKER_PARAM)?: PickerParam()

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
            onPageViewByType()
        } else {
            onPermissionPageView()
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

    private fun onFinishIntent(data: PickerResult) {
        val intent = Intent()
        intent.putExtra(EXTRA_RESULT_PICKER, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onEditorIntent(data: PickerResult) {
        /*
        * TODO: we didn't supported the editor yet,
        *  need to change after editor developed on Q3.
        *  */
        onFinishIntent(data)
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        container.cameraFragment().run {
            val cameraFragment = this

            if (cameraFragment != null && cameraFragment.isAdded) {
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

        val intent = Intent(this, PickerPreviewActivity::class.java).apply {
            putExtra(EXTRA_INTENT_PREVIEW, ArrayList(medias))
        }

        startActivityForResult(intent, REQUEST_PREVIEW_PAGE)
    }

    override fun onCameraThumbnailClicked() {
        onContinueClicked()
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
        return model.isMoreThan(param.get().maxVideoFileSize())
    }

    override fun isMinImageResolution(model: MediaUiModel): Boolean {
        return model.isMinImageRes(param.get().minImageResolution())
    }

    override fun isMaxImageResolution(model: MediaUiModel): Boolean {
        return model.isMaxImageRes(param.get().maxImageResolution())
    }

    override fun isMaxImageSize(model: MediaUiModel): Boolean {
        return model.isMoreThan(param.get().maxImageFileSize())
    }

    override fun isMinStorageThreshold(): Boolean {
        return viewModel.isDeviceStorageFull()
    }

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
        onShowValidationToaster(
            R.string.picker_video_duration_min_limit,
            param.get().minVideoDuration().toSec()
        )

        pickerAnalytics.minVideoDuration()
    }

    override fun onShowVideoMaxDurationToast() {
        onShowValidationToaster(
            R.string.picker_video_duration_max_limit,
            param.get().maxVideoDuration().toSec().toVideoMaxDurationTextFormat(this)
        )

        pickerAnalytics.maxVideoDuration()
    }

    override fun onShowVideoMaxFileSizeToast() {
        onShowValidationToaster(
            R.string.picker_video_max_size,
            param.get().maxVideoFileSize().toMb()
        )

        pickerAnalytics.maxVideoSize()
    }

    override fun onShowImageMaxFileSizeToast() {
        onShowValidationToaster(
            R.string.picker_image_max_size,
            param.get().maxImageFileSize().toMb()
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
        return PickerFragmentFactoryImpl()
    }

    protected open fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    companion object {
        const val REQUEST_PREVIEW_PAGE = 123

        private const val LAST_MEDIA_SELECTION = "last_media_selection"
    }

}