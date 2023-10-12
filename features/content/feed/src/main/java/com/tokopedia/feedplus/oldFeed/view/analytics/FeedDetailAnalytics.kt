package com.tokopedia.feedplus.oldFeed.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.feedplus.oldFeed.view.constants.Constants
import com.tokopedia.feedplus.oldFeed.view.fragment.FeedPlusDetailFragment.Companion.KEY_OTHER
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.track.interfaces.ContextAnalytics

class FeedDetailAnalytics {
    companion object {
        val feedDetailAnalytics: FeedDetailAnalytics by lazy { FeedDetailAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun getMoEngage(): ContextAnalytics {
        return TrackApp.getInstance().moEngage
    }

    fun eventShareCategory(parentCat: String, label: String?) {
        getTracker().sendGeneralEvent(
                Constants.FeedDetailConstants.Event.CATEGORY_PAGE,
                Constants.FeedDetailConstants.Category.CATEGORY_PAGE + "-" + parentCat,
                Constants.FeedDetailConstants.Action.CATEGORY_SHARE,
                label)
    }

    fun sendAnalyticsToGtm(type: String) {
        when (type) {
            LinkerData.REFERRAL_TYPE -> {
                sendEventReferralAndShare(
                        Constants.FeedDetailConstants.Action.SELECT_CHANNEL,
                        KEY_OTHER
                )
                sendMoEngageReferralShareEvent(KEY_OTHER)
            }
            LinkerData.APP_SHARE_TYPE -> sendEventAppShareWhenReferralOff(
                    Constants.FeedDetailConstants.Action.SELECT_CHANNEL,
                    KEY_OTHER
            )
            else -> sendEventShare(KEY_OTHER)
        }
    }

    private fun sendMoEngageReferralShareEvent(channel: String?) {
        val value = DataLayer.mapOf(
                Constants.FeedDetailConstants.MoEngage.CHANNEL, channel
        )
        getMoEngage().sendTrackEvent(value, Constants.FeedDetailConstants.EventMoEngage.REFERRAL_SHARE_EVENT)
    }

    private fun sendEventReferralAndShare(action: String, label: String) {
        val eventTracking: MutableMap<String, Any> = HashMap()
        eventTracking["event"] = Constants.FeedDetailConstants.Event.CLICK_APP_SHARE_REFERRAL
        eventTracking["eventCategory"] = Constants.FeedDetailConstants.Category.REFERRAL
        eventTracking["eventAction"] = action
        eventTracking["eventLabel"] = label
        getTracker().sendGeneralEvent(eventTracking)
    }

    private fun sendEventAppShareWhenReferralOff(action: String, label: String) {
        val eventTracking: MutableMap<String, Any> = HashMap()
        eventTracking["event"] = Constants.FeedDetailConstants.Event.CLICK_APP_SHARE_WHEN_REFERRAL_OFF
        eventTracking["eventCategory"] = Constants.FeedDetailConstants.Category.APPSHARE
        eventTracking["eventAction"] = action
        eventTracking["eventLabel"] = label
        getTracker().sendGeneralEvent(eventTracking)
    }

    private fun sendEventShare(label: String) {
        val eventTracking: MutableMap<String, Any> = HashMap()
        eventTracking["event"] = Constants.FeedDetailConstants.Event.PRODUCT_DETAIL_PAGE
        eventTracking["eventCategory"] = Constants.FeedDetailConstants.Category.PRODUCT_DETAIL
        eventTracking["eventAction"] = Constants.FeedDetailConstants.Action.CLICK
        eventTracking["eventLabel"] = Constants.FeedDetailConstants.EventLabel.SHARE_TO + label
        getTracker().sendGeneralEvent(eventTracking)
    }
}
