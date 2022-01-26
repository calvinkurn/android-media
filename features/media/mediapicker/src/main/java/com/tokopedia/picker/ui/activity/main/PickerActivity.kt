package com.tokopedia.picker.ui.activity.main

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.common.component.uiComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.picker.R
import com.tokopedia.picker.common.PickerFragmentType
import com.tokopedia.picker.common.PickerPageType
import com.tokopedia.picker.databinding.ActivityPickerBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerFragmentFactory
import com.tokopedia.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.PickerNavigator
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.component.NavToolbarComponent
import com.tokopedia.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.utils.EventState
import com.tokopedia.picker.utils.addOnTabSelected
import com.tokopedia.picker.utils.delegates.permissionGranted
import com.tokopedia.picker.utils.dimensionPixelOffsetOf
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
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
        setupQueryAndUIConfigBuilder()
        restoreDataState(savedInstanceState)

        initInjector()
        initView()
        initObservable()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        medias.distinct().forEach {
            println("MEDIAPICKER -> ${it.path}")
        }
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
        savedInstanceState?.let {
            // restore the last media selection to the drawer
            it.getParcelableArrayList<MediaUiModel>(LAST_MEDIA_SELECTION)?.let { elements ->
//                viewModel.publishSelectionDataChanged(elements)
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
        lifecycle.addObserver(viewModel)

        lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.collect {
                when (it) {
                    is EventState.SelectionChanged -> {
                        medias.clear()
                        medias.addAll(it.data)
                    }
                    is EventState.CameraCaptured -> {
                        it.data?.let { media -> medias.add(media) }
                        navToolbar.showContinueButton()
                    }
                    is EventState.SelectionAdded -> {
                        medias.add(it.data)
                    }
                    is EventState.SelectionRemoved -> {
                        medias.remove(it.media)
                    }
                }
            }

            navToolbar.showContinueButtonWithCondition(
                medias.isNotEmpty()
            )
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
                navToolbar.setNavToolbarColorState(true)
                navigator?.start(PickerFragmentType.CAMERA)
            }
            PickerPageType.GALLERY -> {
                navToolbar.setNavToolbarColorState(false)
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
        navToolbar.setNavToolbarColorState(true)

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
        navToolbar.setNavToolbarColorState(isTransparent = true)
        binding?.container?.setMargin(0, 0, 0, 0)
    }

    private fun onGalleryTabSelected() {
        val marginBottom = dimensionPixelOffsetOf(R.dimen.picker_page_margin_bottom)

        navigator?.onPageSelected(PickerFragmentType.GALLERY)
        navToolbar.setNavToolbarColorState(isTransparent = false)

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
        private const val LAST_MEDIA_SELECTION = "last_media_selection"

        private const val PAGE_CAMERA_INDEX = 0
        private const val PAGE_GALLERY_INDEX = 1
    }

}