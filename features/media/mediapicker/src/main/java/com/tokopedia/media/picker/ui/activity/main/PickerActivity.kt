package com.tokopedia.media.picker.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ActivityPickerBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.media.picker.ui.PickerNavigator
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.uimodel.hasVideoBy
import com.tokopedia.media.picker.ui.uimodel.safeRemove
import com.tokopedia.media.picker.utils.addOnTabSelected
import com.tokopedia.media.picker.utils.delegates.permissionGranted
import com.tokopedia.media.picker.utils.dimensionPixelOffsetOf
import com.tokopedia.media.picker.utils.setBottomMargin
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.intent.PickerIntent.KEY_PICKER_PARAM
import com.tokopedia.picker.common.intent.PreviewIntent
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.types.FragmentType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.toUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner.cleanUpInternalStorageIfNeeded
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.view.binding.viewBinding
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
open class PickerActivity : BaseActivity()
    , PermissionFragment.Listener
    , NavToolbarComponent.Listener
    , PickerActivityListener {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var cacheManager: ParamCacheManager

    private val binding: ActivityPickerBinding? by viewBinding()
    private val hasPermissionGranted: Boolean by permissionGranted()

    protected val medias = arrayListOf<MediaUiModel>()

    private val param by lazy {
        cacheManager.getParam()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[PickerViewModel::class.java]
    }

    private val navigator: PickerNavigator? by lazy {
        PickerNavigator(
            this,
            R.id.container,
            supportFragmentManager,
            createFragmentFactory()
        )
    }

    private val navToolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it,
            useArrowIcon = false
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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PREVIEW_PAGE && data != null) {
            // get data from preview if user had an updated the media elements
            val mediasFromPreview = data.getParcelableArrayListExtra<MediaUiModel>(
                PickerPreviewActivity.MEDIA_ELEMENT_RESULT
            )?.toList()?: return

            stateOnChangePublished(mediasFromPreview)
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

    override fun onPermissionGranted() {
        navigateByPageType()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        navigator?.cameraFragment()?.gestureDetector?.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onCloseClicked() {
        finish()
    }

    override fun onContinueClicked() {
        val intent = PreviewIntent.intent(this, medias)
        startActivityForResult(intent, REQUEST_PREVIEW_PAGE)
    }

    private fun setupParamQueryAndDataIntent() {
        val pickerParam = intent.getParcelableExtra(KEY_PICKER_PARAM)?: PickerParam()

        intent?.data?.let {
            PickerUiConfig.setupQueryPage(it)
            PickerUiConfig.setupQueryMode(it)
            PickerUiConfig.setupQuerySelectionType(it)
            PickerUiConfig.setupQueryLandingPageIndex(it)

            pickerParam.apply {
                pageType(PickerUiConfig.pageType)
                modeType(PickerUiConfig.modeType)
                asMultipleSelectionMode(PickerUiConfig.isMultipleSelectionMode)
            }
        }

        // set the picker param as cache
        cacheManager.setParam(pickerParam)

        // get pre-included media items
        param.includeMedias()
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
            navigator?.open(FragmentType.PERMISSION)
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
                    if (!cacheManager.getParam().isMultipleSelectionType()) {
                        medias.clear()
                    }

                    if (!medias.contains(it)) {
                        medias.add(it)
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
        when (PickerUiConfig.pageType) {
            PageType.CAMERA -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
                navigator?.open(FragmentType.CAMERA)
            }
            PageType.GALLERY -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
                navigator?.open(FragmentType.GALLERY)
            }
            else -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)

                // display the tab navigation
                setupTabView()

                // start position of tab
                onTabStartPositionChanged()
            }
        }
    }

    private fun onTabStartPositionChanged() {
        var position = PickerUiConfig.startPageIndex
        val tabCount = binding?.tabPage?.tabLayout?.tabCount.orZero()

        if (position > tabCount) {
            position = 0
        }

        binding?.tabPage?.tabLayout?.getTabAt(position)?.select()
        onTabSelectionChanged(position)
    }

    private fun setupTabView() {
        binding?.tabContainer?.show()

        // setup as transparent tab layout background
        binding?.tabPage?.tabLayout?.setBackgroundColor(Color.TRANSPARENT)

        binding?.tabPage?.addNewTab(getString(R.string.picker_title_camera))
        binding?.tabPage?.addNewTab(getString(R.string.picker_title_gallery))

        binding?.tabPage?.tabLayout?.addOnTabSelected(
            ::onTabSelectionChanged
        )
    }

    private fun onTabSelectionChanged(position: Int) {
        if (position == PAGE_CAMERA_INDEX) {
            onCameraTabSelected()
        } else if (position == PAGE_GALLERY_INDEX) {
            onGalleryTabSelected()
        }
    }

    private fun onCameraTabSelected() {
        navigator?.open(FragmentType.CAMERA)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        binding?.container?.setBottomMargin(0)
    }

    private fun onGalleryTabSelected() {
        navigator?.open(FragmentType.GALLERY)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)

        val marginBottom = dimensionPixelOffsetOf(R.dimen.picker_page_margin_bottom)
        binding?.container?.setBottomMargin(marginBottom)
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

    override fun tabVisibility(isShown: Boolean) {
        if (!param.isCommonPageType()) return
        binding?.tabContainer?.showWithCondition(isShown)
    }

    override fun mediaSelected(): List<MediaUiModel> {
        return medias
    }

    override fun hasVideoLimitReached(): Boolean {
        return medias.hasVideoBy(param.maxVideoCount())
    }

    override fun hasMediaLimitReached(): Boolean {
        return medias.size == param.maxMediaAmount()
    }

    override fun isMinVideoDuration(model: MediaUiModel): Boolean {
        return model.getVideoDuration(applicationContext) <= param.minVideoDuration()
    }

    override fun isMaxVideoDuration(model: MediaUiModel): Boolean {
        return model.getVideoDuration(applicationContext) > param.maxVideoDuration()
    }

    override fun isMaxVideoSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.maxVideoSize())
    }

    override fun isMinImageResolution(model: MediaUiModel): Boolean {
        return model.isMinImageRes(param.minImageResolution())
    }

    override fun isMaxImageResolution(model: MediaUiModel): Boolean {
        return model.isMaxImageRes(param.maxImageResolution())
    }

    override fun isMaxImageSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.maxImageSize())
    }

    override fun onShowMediaLimitReachedToast() {
        onShowToaster(R.string.picker_selection_limit_message, param.maxMediaAmount())
    }

    override fun onShowVideoLimitReachedToast() {
        onShowToaster(R.string.picker_selection_limit_video, param.maxVideoCount())
    }

    override fun onShowVideoMinDurationToast() {
        onShowToaster(R.string.picker_video_duration_min_limit, param.minVideoDuration())
    }

    override fun onShowVideoMaxDurationToast() {
        onShowToaster(R.string.picker_video_duration_max_limit, param.maxVideoDuration())
    }

    override fun onShowVideoMaxFileSizeToast() {
        onShowToaster(R.string.picker_video_max_size, param.maxVideoSize())
    }

    override fun onShowImageMinResToast() {
        onShowToaster(R.string.picker_image_res_min_limit, param.maxImageResolution())
    }

    override fun onShowImageMaxResToast() {
        onShowToaster(R.string.picker_image_res_max_limit, param.minImageResolution())
    }

    override fun onShowImageMaxFileSizeToast() {
        onShowToaster(R.string.picker_image_max_size, param.maxImageSize())
    }

    private fun onShowToaster(messageId: Int, param: Number) {
        binding?.rootView?.let {
            val content = getString(messageId, param)
            Toaster.build(it, content, Toaster.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_PREVIEW_PAGE = 123

        private const val LAST_MEDIA_SELECTION = "last_media_selection"

        private const val PAGE_CAMERA_INDEX = 0
        private const val PAGE_GALLERY_INDEX = 1
    }

}