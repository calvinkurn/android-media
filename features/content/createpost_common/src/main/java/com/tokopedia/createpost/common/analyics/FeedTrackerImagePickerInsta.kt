package com.tokopedia.createpost.common.analyics

import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta.Action.CLICK_BACK_ON_PICKER_PAGE
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta.Action.CLICK_CAMERA_ON_PICKER_PAGE
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta.Action.CLICK_CIRCLE_ON_PICKER_PAGE
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta.Action.CLICK_NEXT_ON_PICKER_PAGE
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta.Category.CONTENT_FEED_CREATION
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta.Event.CLICK_FEED
import com.tokopedia.imagepicker_insta.common.trackers.TrackerContract

class FeedTrackerImagePickerInsta(val shopId: String) : TrackerContract {

    companion object {
        private val PARAM_EVENT_NAME = "event"
        private val PARAM_EVENT_CATEGORY = "eventCategory"
        private val PARAM_EVENT_ACTION = "eventAction"
        private val PARAM_EVENT_LABEL = "eventLabel"
        private const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        private const val KEY_CURRENT_SITE_EVENT = "currentSite"
        private const val MARKETPLACE = "tokopediamarketplace"
        private const val KEY_SESSION_IRIS = "sessionIris"
        private const val SESSION_IRIS_VALUE = "sessioniris"
        private const val CONTENT = "content"

    }
    private object Event {
        const val CLICK_FEED = "clickFeed"
    }
    private object Category {
        const val CONTENT_FEED_CREATION = "content feed creation"
    }
    private object Action {
        const val CLICK_NEXT_ON_PICKER_PAGE = "click next on picker page"
        const val CLICK_BACK_ON_PICKER_PAGE = "click back on picker page"
        const val CLICK_CAMERA_ON_PICKER_PAGE = "click camera on picker page"
        const val CLICK_CIRCLE_ON_PICKER_PAGE = "click circle on record page"
    }
    override fun onNextButtonClick() {
        val map = mutableMapOf<String, Any>()
        map[PARAM_EVENT_NAME] = CLICK_FEED
        map[PARAM_EVENT_ACTION] = CLICK_NEXT_ON_PICKER_PAGE
        map[PARAM_EVENT_CATEGORY] = CONTENT_FEED_CREATION
        map[PARAM_EVENT_LABEL] = shopId
        map[KEY_BUSINESS_UNIT_EVENT] = CONTENT
        map[KEY_CURRENT_SITE_EVENT] = MARKETPLACE
        getTracker().sendGeneralEvent(map)
    }

    override fun onBackButtonFromPicker() {
        val map = mutableMapOf<String, Any>()
        map[PARAM_EVENT_NAME] = CLICK_FEED
        map[PARAM_EVENT_ACTION] = CLICK_BACK_ON_PICKER_PAGE
        map[PARAM_EVENT_CATEGORY] = CONTENT_FEED_CREATION
        map[PARAM_EVENT_LABEL] = shopId
        map[KEY_BUSINESS_UNIT_EVENT] = CONTENT
        map[KEY_CURRENT_SITE_EVENT] = MARKETPLACE
        map[KEY_CURRENT_SITE_EVENT] = MARKETPLACE
        getTracker().sendGeneralEvent(map)
    }

    override fun onCameraButtonFromPickerClick() {
        val map = mutableMapOf<String, Any>()
        map[PARAM_EVENT_NAME] = CLICK_FEED
        map[PARAM_EVENT_ACTION] = CLICK_CAMERA_ON_PICKER_PAGE
        map[PARAM_EVENT_CATEGORY] = CONTENT_FEED_CREATION
        map[PARAM_EVENT_LABEL] = shopId
        map[KEY_BUSINESS_UNIT_EVENT] = CONTENT
        map[KEY_CURRENT_SITE_EVENT] = MARKETPLACE
        getTracker().sendGeneralEvent(map)
    }

    override fun onRecordButtonClick(mediaType:String) {
        val map = mutableMapOf<String, Any>()
        map[PARAM_EVENT_NAME] = CLICK_FEED
        map[PARAM_EVENT_ACTION] = CLICK_CIRCLE_ON_PICKER_PAGE
        map[PARAM_EVENT_CATEGORY] = CONTENT_FEED_CREATION
        map[PARAM_EVENT_LABEL] = "$shopId - $mediaType"
        map[KEY_BUSINESS_UNIT_EVENT] = CONTENT
        map[KEY_CURRENT_SITE_EVENT] = MARKETPLACE
        getTracker().sendGeneralEvent(map)
    }
}