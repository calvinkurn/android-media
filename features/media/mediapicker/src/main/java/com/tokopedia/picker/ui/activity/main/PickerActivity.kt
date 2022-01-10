package com.tokopedia.picker.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.common.PickerFragmentType
import com.tokopedia.picker.common.PickerPageType
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.databinding.ActivityPickerBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerFragmentFactory
import com.tokopedia.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.PickerNavigator
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.picker.utils.G500
import com.tokopedia.picker.utils.N600
import com.tokopedia.picker.utils.addOnTabSelected
import com.tokopedia.picker.utils.delegates.permissionGranted
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
class PickerActivity : BaseActivity(), PermissionFragment.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: ActivityPickerBinding? by viewBinding()
    private val hasPermissionGranted: Boolean by permissionGranted()

    private val selectedMedias: MutableList<Media> = mutableListOf()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        setupQueryAndUIConfigBuilder()

        initInjector()
        initView()
        initObservable()
        initToolbar()
    }

    override fun onPermissionGranted() {
        navigateByPageType()
    }

    private fun setupQueryAndUIConfigBuilder() {
        val data = intent?.data ?: return

        PickerUiConfig.setupQueryPage(data)
        PickerUiConfig.setupQueryMode(data)
        PickerUiConfig.setupQuerySelectionType(data)
    }

    private fun initView() {
        if (hasPermissionGranted) {
            permissionGrantedState()
            navigateByPageType()
            return
        }

        navigator?.start(PickerFragmentType.PERMISSION)
        permissionDeniedState()
    }

    private fun initObservable() {
        lifecycle.addObserver(viewModel)

        viewModel.finishButtonState.observe(this) {
            val color = if (it) G500 else N600

            binding?.toolbar?.btnDone?.setTextColor(
                ContextCompat.getColor(applicationContext, color)
            )
        }

        viewModel.selectedMedia.observe(this) {
            selectedMedias.clear()
            selectedMedias.addAll(it)
        }
    }

    private fun initToolbar() {
        binding?.toolbar?.btnDone?.show()
        binding?.toolbar?.btnDone?.setOnClickListener {
            selectedMedias.forEach {
                println("MEDIAPICKER -> ${it.path}")
            }
        }
    }

    private fun permissionGrantedState() {
        binding?.toolbarContainer?.show()
        binding?.tabContainer?.show()
    }

    private fun permissionDeniedState() {
        binding?.toolbarContainer?.hide()
        binding?.tabContainer?.hide()
    }

    private fun navigateByPageType() {
        when (PickerUiConfig.paramPage) {
            PickerPageType.CAMERA -> navigator?.start(PickerFragmentType.CAMERA)
            PickerPageType.GALLERY -> navigator?.start(PickerFragmentType.GALLERY)
            else -> {
                // show camera as first page
                navigator?.start(PickerFragmentType.CAMERA)

                // display the tab navigation
                setupTabView()
            }
        }
    }

    private fun setupTabView() {
        binding?.tabContainer?.addNewTab(getString(R.string.picker_title_camera))
        binding?.tabContainer?.addNewTab(getString(R.string.picker_title_gallery))
        binding?.tabContainer?.show()

        binding?.tabContainer?.tabLayout?.addOnTabSelected { position ->
            if (position == 0) {
                navigator?.onPageSelected(PickerFragmentType.CAMERA)
            } else if (position == 1) {
                navigator?.onPageSelected(PickerFragmentType.GALLERY)
            }
        }
    }

    private fun createFragmentFactory(): PickerFragmentFactory {
        return PickerFragmentFactoryImpl()
    }

    private fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .pickerModule(PickerModule())
            .build()
            .inject(this)
    }

    companion object {
        //TODO remove
        fun start(context: Context) {
            context.startActivity(Intent(context, PickerActivity::class.java))
        }
    }

}