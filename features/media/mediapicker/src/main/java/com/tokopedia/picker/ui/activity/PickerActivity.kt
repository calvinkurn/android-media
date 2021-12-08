package com.tokopedia.picker.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst.MediaPicker.*
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.common.PickerModeType
import com.tokopedia.picker.ui.common.PickerPageType
import com.tokopedia.picker.ui.common.PickerSelectionType
import com.tokopedia.picker.ui.fragment.PickerFragmentFactory
import com.tokopedia.picker.ui.fragment.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.fragment.PickerNavigator
import com.tokopedia.picker.ui.fragment.PickerUiConfig
import com.tokopedia.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil.checkHasPermission as hasPermission

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

    private var navigator: PickerNavigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        setupNavigator()
        setupQueryParameter()
        setupInitialPage()
    }

    override fun granted() {
        navigator?.onPageSelected(PickerFragmentType.PICKER)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator?.cleanUp()
    }

    private fun setupNavigator() {
        navigator = PickerNavigator(
            this,
            R.id.container,
            supportFragmentManager,
            createFragmentFactory()
        )
    }

    private fun setupQueryParameter() {
        val data = intent?.data ?: return
        setupQueryPage(data)
        setupQueryMode(data)
        setupQuerySelectionType(data)
    }

    private fun setupQueryPage(data: Uri) {
        val page = when(data.getQueryParameter(PARAM_PAGE)) {
            VALUE_PAGE_CAMERA -> PickerPageType.CAMERA
            VALUE_PAGE_GALLERY -> PickerPageType.GALLERY
            else -> PickerPageType.COMMON
        }

        PickerUiConfig.paramPage = page
    }

    private fun setupQueryMode(data: Uri) {
        val mode = when(data.getQueryParameter(PARAM_MODE)) {
            VALUE_MODE_IMAGE -> PickerModeType.IMAGE_ONLY
            VALUE_MODE_VIDEO -> PickerModeType.VIDEO_ONLY
            else -> PickerModeType.COMMON
        }

        PickerUiConfig.paramMode = mode
    }

    private fun setupQuerySelectionType(data: Uri) {
        val selectionType = when(data.getQueryParameter(PARAM_SELECTION)) {
            VALUE_TYPE_SINGLE -> PickerSelectionType.SINGLE
            VALUE_TYPE_MULTIPLE -> PickerSelectionType.MULTIPLE
            else -> null
        }

        selectionType?.let {
            PickerUiConfig.paramType = it
        }
    }

    private fun setupInitialPage() {
        val hasPermissionStorage = hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val hasPermissionCamera = hasPermission(this, Manifest.permission.CAMERA)
        navigator?.start(
            if (hasPermissionStorage || hasPermissionCamera) {
                PickerFragmentType.PICKER
            } else {
                PickerFragmentType.PERMISSION
            }
        )
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