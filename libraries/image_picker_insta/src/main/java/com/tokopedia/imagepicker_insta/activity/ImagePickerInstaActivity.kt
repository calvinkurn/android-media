package com.tokopedia.imagepicker_insta.activity

import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.content.common.R as commonR
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.imagepicker_insta.fragment.ImagePickerInstaMainFragment
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.VideoUtil

class ImagePickerInstaActivity : PermissionActivity() {

    var toolbarTitle = ""
    var menuTitle = ""
    var toolbarIconUrl = ""
    var maxMultiSelectAllowed = BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED
    var applinkToNavigateAfterMediaCapture = ""
    var applinkForGalleryProceed = ""
    var applinkForBackNavigation = ""
    var videoMaxDurationInSeconds:Long = VideoUtil.DEFAULT_DURATION_MAX_LIMIT
    var isCreatePostAsBuyer: Boolean = false
    var isOpenFrom = ""

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
        val defaultTitle = getString(commonR.string.feed_content_post_sebagai)
        toolbarTitle = intent.extras?.getString(BundleData.TITLE, defaultTitle) ?: defaultTitle
        menuTitle = intent.extras?.getString(BundleData.MENU_TITLE) ?: getString(R.string.imagepicker_insta_lanjut)
        maxMultiSelectAllowed = intent.extras?.getInt(BundleData.MAX_MULTI_SELECT_ALLOWED) ?: BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED
        isOpenFrom = intent.extras?.getString(BundleData.KEY_IS_OPEN_FROM, "") ?: ""
        if (maxMultiSelectAllowed == 0) {
            maxMultiSelectAllowed = BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED
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
