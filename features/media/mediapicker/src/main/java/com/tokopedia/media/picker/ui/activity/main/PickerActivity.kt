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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.common.basecomponent.uiComponent
import com.tokopedia.media.common.component.NavToolbarComponent
import com.tokopedia.media.common.component.ToolbarTheme
import com.tokopedia.media.common.intent.PreviewIntent
import com.tokopedia.media.common.types.PickerFragmentType
import com.tokopedia.media.common.types.PickerPageType
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.databinding.ActivityPickerBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.*
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.media.picker.ui.uimodel.containByName
import com.tokopedia.media.picker.ui.uimodel.safeRemove
import com.tokopedia.media.common.observer.EventFlowFactory
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.utils.addOnTabSelected
import com.tokopedia.media.picker.utils.delegates.permissionGranted
import com.tokopedia.media.picker.utils.dimensionPixelOffsetOf
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
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

    private val binding: ActivityPickerBinding? by viewBinding()
    private val hasPermissionGranted: Boolean by permissionGranted()

    private val param by lazy { PickerUiConfig.pickerParam() }
    private val medias = arrayListOf<MediaUiModel>()

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

        setupQueryAndUIConfigBuilder()
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
        PickerUiConfig.pickerParam = null
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

    override fun tabVisibility(isShown: Boolean) {
        if (!param.isCommonPageType()) return
        binding?.tabContainer?.showWithCondition(isShown)
    }

    override fun mediaSelected(): List<MediaUiModel> {
        return medias
    }

    private fun setupQueryAndUIConfigBuilder() {
        val data = intent?.data ?: return

        PickerUiConfig.setupQueryPage(data)
        PickerUiConfig.setupQueryMode(data)
        PickerUiConfig.setupQuerySelectionType(data)
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
            permissionGrantedState()
            navigateByPageType()
        } else {
            permissionDeniedState()
            navigator?.start(PickerFragmentType.PERMISSION)
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
                    if (medias.containByName(it)) {
                        medias.safeRemove(it)
                    }
                },
                onAdded = {
                    if (PickerUiConfig.isSingleSelectionType()) {
                        medias.clear()
                    }

                    if (!medias.containByName(it)) {
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

    private fun permissionGrantedState() {
        binding?.toolbarContainer?.show()
    }

    private fun permissionDeniedState() {
        binding?.toolbarContainer?.hide()
    }

    private fun navigateByPageType() {
        when (PickerUiConfig.paramPage) {
            PickerPageType.CAMERA -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
                navigator?.start(PickerFragmentType.CAMERA)
            }
            PickerPageType.GALLERY -> {
                navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
                navigator?.start(PickerFragmentType.GALLERY)
            }
            else -> {
                // show camera as initial page
                navigator?.start(PickerFragmentType.CAMERA)

                // display the tab navigation
                setupTabView()
            }
        }
    }

    private fun setupTabView() {
        binding?.tabContainer?.show()

        // setup as transparent tab layout background
        binding?.tabPage?.tabLayout?.setBackgroundColor(Color.TRANSPARENT)

        // set transparent of nav toolbar
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)

        binding?.tabPage?.addNewTab(getString(R.string.picker_title_camera))
        binding?.tabPage?.addNewTab(getString(R.string.picker_title_gallery))

        binding?.tabPage?.tabLayout?.addOnTabSelected { position ->
            if (position == PAGE_CAMERA_INDEX) {
                onCameraTabSelected()
            } else if (position == PAGE_GALLERY_INDEX) {
                onGalleryTabSelected()
            }
        }
    }

    private fun onCameraTabSelected() {
        navigator?.onPageSelected(PickerFragmentType.CAMERA)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        binding?.container?.setMargin(0, 0, 0, 0)
    }

    private fun onGalleryTabSelected() {
        val marginBottom = dimensionPixelOffsetOf(R.dimen.picker_page_margin_bottom)

        navigator?.onPageSelected(PickerFragmentType.GALLERY)
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)

        binding?.container?.setMargin(0, 0, 0, marginBottom)
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
        private const val REQUEST_PREVIEW_PAGE = 123

        private const val LAST_MEDIA_SELECTION = "last_media_selection"

        private const val PAGE_CAMERA_INDEX = 0
        private const val PAGE_GALLERY_INDEX = 1
    }

}