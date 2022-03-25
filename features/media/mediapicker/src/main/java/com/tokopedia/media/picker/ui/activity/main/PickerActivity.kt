package com.tokopedia.media.picker.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
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
    @Inject lateinit var param: ParamCacheManager

    private val binding: ActivityPickerBinding? by viewBinding()
    private val hasPermissionGranted: Boolean by permissionGranted()

    protected val medias = arrayListOf<MediaUiModel>()

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
        onPreviewItemSelected(medias)
    }

    override fun onPreviewItemSelected(medias: List<MediaUiModel>) {
        val intent = Intent(this, PickerPreviewActivity::class.java).apply {
            putExtra(EXTRA_INTENT_PREVIEW, ArrayList(medias))
        }
        startActivityForResult(intent, REQUEST_PREVIEW_PAGE)
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

    }

    override fun tabVisibility(isShown: Boolean) {
        if (!param.get().isCommonPageType()) return
        binding?.tabContainer?.showWithCondition(isShown)
    }

    override fun mediaSelected(): List<MediaUiModel> {
        return medias
    }

    override fun hasVideoLimitReached(): Boolean {
        return medias.hasVideoBy(param.get().maxVideoCount())
    }

    override fun hasMediaLimitReached(): Boolean {
        return medias.size == param.get().maxMediaAmount()
    }

    override fun isMinVideoDuration(model: MediaUiModel): Boolean {
        return model.videoDuration(applicationContext) <= param.get().minVideoDuration()
    }

    override fun isMaxVideoDuration(model: MediaUiModel): Boolean {
        return model.videoDuration(applicationContext) > param.get().maxVideoDuration()
    }

    override fun isMaxVideoSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.get().maxVideoSize())
    }

    override fun isMinImageResolution(model: MediaUiModel): Boolean {
        return model.isMinImageRes(param.get().minImageResolution())
    }

    override fun isMaxImageResolution(model: MediaUiModel): Boolean {
        return model.isMaxImageRes(param.get().maxImageResolution())
    }

    override fun isMaxImageSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.get().maxImageSize())
    }

    override fun onShowMediaLimitReachedToast() {
        onShowValidationToaster(
            R.string.picker_selection_limit_message,
            param.get().maxMediaAmount()
        )
    }

    override fun onShowVideoLimitReachedToast() {
        onShowValidationToaster(
            R.string.picker_selection_limit_video,
            param.get().maxVideoCount()
        )
    }

    override fun onShowVideoMinDurationToast() {
        onShowValidationToaster(
            R.string.picker_video_duration_min_limit,
            param.get().minVideoDuration().toSec()
        )
    }

    override fun onShowVideoMaxDurationToast() {
        onShowValidationToaster(
            R.string.picker_video_duration_max_limit,
            param.get().maxVideoDuration().toSec().toVideoMaxDurationTextFormat(this)
        )
    }

    override fun onShowVideoMaxFileSizeToast() {
        onShowValidationToaster(
            R.string.picker_video_max_size,
            param.get().maxVideoSize().toMb()
        )
    }

    override fun onShowImageMaxFileSizeToast() {
        onShowValidationToaster(
            R.string.picker_image_max_size,
            param.get().maxImageSize().toMb()
        )
    }

    override fun onShowImageMinResToast() {
        onShowValidationToaster(
            R.string.picker_image_res_min_limit,
            param.get().maxImageResolution()
        )
    }

    override fun onShowImageMaxResToast() {
        onShowValidationToaster(
            R.string.picker_image_res_max_limit,
            param.get().minImageResolution()
        )
    }

    private fun onShowValidationToaster(messageId: Int, param: Any) {
        val content = getString(messageId, param)
        onShowToaster(content)
    }

    private fun onShowToaster(message: String) {
        binding?.rootView?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT).show()
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

    override fun navigateToCameraPage() {
        binding?.tabPage?.tabLayout?.getTabAt(PAGE_CAMERA_INDEX)?.select()
    }

    companion object {
        const val REQUEST_PREVIEW_PAGE = 123

        private const val LAST_MEDIA_SELECTION = "last_media_selection"

        private const val PAGE_CAMERA_INDEX = 0
        private const val PAGE_GALLERY_INDEX = 1
    }

}