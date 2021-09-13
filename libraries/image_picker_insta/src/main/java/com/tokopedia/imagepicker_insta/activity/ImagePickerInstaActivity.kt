package com.tokopedia.imagepicker_insta.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.fragment.ImagePickerInstaMainFragment
import com.tokopedia.imagepicker_insta.models.BundleData
import com.tokopedia.imagepicker_insta.util.PermissionUtil

class ImagePickerInstaActivity : AppCompatActivity() {

    var toolbarTitle = ""
    var toolbarSubTitle = ""
    var menuTitle = ""
    var toolbarIconRes = 0
    var toolbarIconUrl = ""
    var maxMultiSelectAllowed = MAX_MULTI_SELECT_LIMIT
    var applinkToNavigateAfterMediaCapture = ""
    var applinkForGalleryProceed = ""
    var applinkForBackNavigation = ""

    companion object {

        internal const val MAX_MULTI_SELECT_LIMIT = 5

        fun getIntent(
            context: Context, title: String? = null,
            subtitle: String? = null,
            toolbarIconRes: Int? = null,
            toolbarIconUrl: String? = null,
            menuTitle: String? = null,
            @IntRange(from = 1L, to = MAX_MULTI_SELECT_LIMIT.toLong())
            maxMultiSelectAllowed: Int = 5,
            applinkToNavigateAfterMediaCapture: String? = null,
            applinkForGalleryProceed: String? = null,
            applinkForBackNavigation: String? = null,
        ): Intent {
            val intent = Intent(context, ImagePickerInstaActivity::class.java)
            intent.putExtra(BundleData.TITLE, title)
            intent.putExtra(BundleData.SUB_TITLE, subtitle)
            intent.putExtra(BundleData.TOOLBAR_ICON_RES, toolbarIconRes)
            intent.putExtra(BundleData.TOOLBAR_ICON_URL, toolbarIconUrl)
            intent.putExtra(BundleData.MENU_TITLE, menuTitle)
            intent.putExtra(BundleData.MAX_MULTI_SELECT_ALLOWED, Math.min(maxMultiSelectAllowed, MAX_MULTI_SELECT_LIMIT))
            intent.putExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, applinkToNavigateAfterMediaCapture)
            intent.putExtra(BundleData.APPLINK_FOR_GALLERY_PROCEED, applinkForGalleryProceed)
            intent.putExtra(BundleData.APPLINK_FOR_BACK_NAVIGATION, applinkForBackNavigation)
            return intent
        }
    }

    var cameraPermissionCallback: ((Boolean) -> Unit)? = null
    var canRequestPermissionFromOnResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processIntentData()
        setContentView(R.layout.imagepicker_insta_activity_main)

        if (PermissionUtil.isReadPermissionGranted(this)) {
            getAttachedFragment()?.showDataUi()
        } else {
            getAttachedFragment()?.let {
                PermissionUtil.requestReadPermission(it)
            }
        }
    }

    fun getAttachedFragment():ImagePickerInstaMainFragment?{
        if(!supportFragmentManager.fragments.isNullOrEmpty()){
            return supportFragmentManager.fragments.first() as? ImagePickerInstaMainFragment
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        if (canRequestPermissionFromOnResume && !PermissionUtil.isReadPermissionGranted(this)) {
            getAttachedFragment()?.showPermissionUi()
            canRequestPermissionFromOnResume = false
        } else {
            canRequestPermissionFromOnResume = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    getAttachedFragment()?.showDataUi()
                } else {
                    getAttachedFragment()?.showPermissionUi()
                }
            }
            PermissionUtil.CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE -> {
                cameraPermissionCallback?.invoke(PermissionUtil.hasAllPermission(this))
            }
        }
    }

    private fun processIntentData() {
        toolbarTitle = intent.extras?.getString(BundleData.TITLE, "") ?: ""
        toolbarSubTitle = intent.extras?.getString(BundleData.SUB_TITLE, "") ?: ""
        toolbarIconRes = intent.extras?.getInt(BundleData.TOOLBAR_ICON_RES) ?: 0
        menuTitle = intent.extras?.getString(BundleData.MENU_TITLE) ?: getString(R.string.imagepicker_insta_lanjut)
        maxMultiSelectAllowed = intent.extras?.getInt(BundleData.MAX_MULTI_SELECT_ALLOWED) ?: MAX_MULTI_SELECT_LIMIT
        applinkToNavigateAfterMediaCapture = intent.extras?.getString(BundleData.APPLINK_AFTER_CAMERA_CAPTURE) ?: ""
        applinkForGalleryProceed = intent.extras?.getString(BundleData.APPLINK_FOR_GALLERY_PROCEED) ?: ""
        applinkForBackNavigation = intent.extras?.getString(BundleData.APPLINK_FOR_BACK_NAVIGATION) ?: ""
        toolbarIconUrl = intent.extras?.getString(BundleData.TOOLBAR_ICON_URL) ?: ""
    }

    override fun finish() {
        super.finish()
        if (!applinkForBackNavigation.isNullOrEmpty()) {
            RouteManager.route(this, applinkForBackNavigation)
        }
    }
}