package com.tokopedia.picker.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst.MediaPicker.*
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.ActivityPickerBinding
import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.common.PickerModeType
import com.tokopedia.picker.ui.common.PickerPageType
import com.tokopedia.picker.ui.common.PickerSelectionType
import com.tokopedia.picker.ui.fragment.PickerFragmentFactory
import com.tokopedia.picker.ui.fragment.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.fragment.PickerNavigator
import com.tokopedia.picker.ui.fragment.PickerUiConfig
import com.tokopedia.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.picker.utils.Permissions.hasPermissionGranted
import com.tokopedia.picker.utils.addOnTabSelected
import com.tokopedia.utils.view.binding.viewBinding

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

    private val binding: ActivityPickerBinding? by viewBinding()

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
        setupQueryParameter()
        setupInitialPage()
    }

    override fun onPermissionGranted() {
        setupPickerByPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator?.cleanUp()
    }

    private fun setupQueryParameter() {
        val data = intent?.data ?: return
        setupQueryPage(data)
        setupQueryMode(data)
        setupQuerySelectionType(data)
    }

    /**
     * queryPage is to specify the desired page type.
     * mediapicker has options to set for:
     * 1. camera page only
     * 2. gallery page only
     * 3. camera & gallery
     *
     * the data comes from:
     * tokopedia://media-picker?page=...
     */
    private fun setupQueryPage(data: Uri) {
        PickerUiConfig.paramPage = when(data.getQueryParameter(PARAM_PAGE)) {
            VALUE_PAGE_CAMERA -> PickerPageType.CAMERA
            VALUE_PAGE_GALLERY -> PickerPageType.GALLERY
            else -> PickerPageType.COMMON
        }
    }

    /**
     * queryMode is to determine the type of media should be display.
     * this also have ability to set the camera mode (camera only, video only, or both).
     * we have 3 options for the type of media, such as:
     * 1. image only
     * 2. video only
     * 3. image & video
     *
     * the data comes from:
     * tokopedia://media-picker?mode=...
     */
    private fun setupQueryMode(data: Uri) {
        PickerUiConfig.paramMode = when(data.getQueryParameter(PARAM_MODE)) {
            VALUE_MODE_IMAGE -> PickerModeType.IMAGE_ONLY
            VALUE_MODE_VIDEO -> PickerModeType.VIDEO_ONLY
            else -> PickerModeType.COMMON
        }
    }

    /**
     * querySelection is to specify the type of selection mode of picker.
     * you can set the selection type as [PickerSelectionType.SINGLE] or
     * as [PickerSelectionType.MULTIPLE].
     *
     * the data comes from:
     * tokopedia://media-picker?type=...
     */
    private fun setupQuerySelectionType(data: Uri) {
        PickerUiConfig.paramType = when(data.getQueryParameter(PARAM_SELECTION)) {
            VALUE_TYPE_SINGLE -> PickerSelectionType.SINGLE
            else -> PickerSelectionType.MULTIPLE // default
        }
    }

    /**
     * set the initial page of picker to display,
     * if the user use android M and above, runtime permission
     * should be required. so, the first thing is, we've to
     * ensure that the permissions has granted.
     */
    private fun setupInitialPage() {
        if (!hasPermissionGranted()) {
            navigator?.start(PickerFragmentType.PERMISSION)
            return
        }

        setupPickerByPage()
    }

    private fun setupPickerByPage() {
        when (PickerUiConfig.paramPage) {
            PickerPageType.CAMERA -> navigator?.onPageSelected(PickerFragmentType.CAMERA)
            PickerPageType.GALLERY -> navigator?.onPageSelected(PickerFragmentType.GALLERY)
            else -> {
                // show camera as first page
                navigator?.onPageSelected(PickerFragmentType.CAMERA)

                // display the tab navigation
                setupTabView()
            }
        }
    }

    private fun setupTabView() {
        binding?.tabContainer?.addNewTab(getString(com.tokopedia.picker.R.string.media_picker_camera))
        binding?.tabContainer?.addNewTab(getString(com.tokopedia.picker.R.string.media_picker_gallery))
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

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PickerActivity::class.java))
        }
    }

}