package com.tokopedia.imagepicker_insta.activity

import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.common.BundleData
import com.tokopedia.imagepicker_insta.common.ImagePickerRouter.DEFAULT_MULTI_SELECT_LIMIT
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.imagepicker_insta.fragment.ImagePickerInstaMainFragment
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.VideoUtil

class ImagePickerInstaActivity : PermissionActivity() {

    var toolbarTitle = ""
    var toolbarSubTitle = ""
    var menuTitle = ""
    var toolbarIconRes = 0
    var toolbarIconUrl = ""
    var maxMultiSelectAllowed = DEFAULT_MULTI_SELECT_LIMIT
    var applinkToNavigateAfterMediaCapture = ""
    var applinkForGalleryProceed = ""
    var applinkForBackNavigation = ""
    var videoMaxDurationInSeconds:Long = VideoUtil.DEFAULT_DURATION_MAX_LIMIT
    var isCreatePostAsBuyer: Boolean = false

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

    fun getAttachedFragment(): ImagePickerInstaMainFragment? {
        if (!supportFragmentManager.fragments.isNullOrEmpty()) {
            return supportFragmentManager.fragments.first() as? ImagePickerInstaMainFragment
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        if (getAttachedFragment()?.isUiInitialized() == true) {
            if (PermissionUtil.isReadPermissionGranted(this)) {
                if (getAttachedFragment()?.isPermissionUiVisible() == true) {
                    getAttachedFragment()?.showDataUi()
                }
            } else {
                getAttachedFragment()?.showPermissionUi()
            }
        }
    }

    private fun processIntentData() {
        toolbarTitle = intent.extras?.getString(BundleData.TITLE, "") ?: ""
        toolbarSubTitle = intent.extras?.getString(BundleData.SUB_TITLE, "") ?: ""
        toolbarIconRes = intent.extras?.getInt(BundleData.TOOLBAR_ICON_RES) ?: 0
        menuTitle = intent.extras?.getString(BundleData.MENU_TITLE) ?: getString(R.string.imagepicker_insta_lanjut)
        maxMultiSelectAllowed = intent.extras?.getInt(BundleData.MAX_MULTI_SELECT_ALLOWED) ?: DEFAULT_MULTI_SELECT_LIMIT
        if (maxMultiSelectAllowed == 0) {
            maxMultiSelectAllowed = DEFAULT_MULTI_SELECT_LIMIT
        }
        applinkToNavigateAfterMediaCapture = intent.extras?.getString(BundleData.APPLINK_AFTER_CAMERA_CAPTURE) ?: ""
        applinkForGalleryProceed = intent.extras?.getString(BundleData.APPLINK_FOR_GALLERY_PROCEED) ?: ""
        applinkForBackNavigation = intent.extras?.getString(BundleData.APPLINK_FOR_BACK_NAVIGATION) ?: ""
        toolbarIconUrl = intent.extras?.getString(BundleData.TOOLBAR_ICON_URL) ?: ""
        videoMaxDurationInSeconds = intent.extras?.getLong(BundleData.VIDEO_MAX_SECONDS) ?: VideoUtil.DEFAULT_DURATION_MAX_LIMIT
        if(videoMaxDurationInSeconds == 0L){
            videoMaxDurationInSeconds = VideoUtil.DEFAULT_DURATION_MAX_LIMIT
        }
        isCreatePostAsBuyer = intent.extras?.getBoolean(BundleData.IS_CREATE_POST_AS_BUYER, false) ?: false
    }

    override fun finish() {
        super.finish()
        if (!applinkForBackNavigation.isNullOrEmpty()) {
            RouteManager.route(this, applinkForBackNavigation)
        }
        TrackerProvider.removeTracker()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        TrackerProvider.tracker?.onBackButtonFromPicker()
        TrackerProvider.removeTracker()
    }
}