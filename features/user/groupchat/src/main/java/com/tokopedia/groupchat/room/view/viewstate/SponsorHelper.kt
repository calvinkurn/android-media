package com.tokopedia.groupchat.room.view.viewstate

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author : Steven 01/07/19
 */
class SponsorHelper(
        model: ChannelInfoViewModel?,
        var sponsorLayout: View,
        var sponsorImage: ImageView,
        var analytics: GroupChatAnalytics,
        var listener: PlayContract.View
): PlayBaseHelper(model) {

    var videoVertical: Boolean = false

    fun setSponsor() {
        val adsId = viewModel?.adsId
        val adsImageUrl = viewModel?.adsImageUrl
        val adsName = viewModel?.adsName
        val videoId = viewModel?.videoId
        if (adsId == null || adsImageUrl.isNullOrBlank()) {
            sponsorLayout.hide()
        } else if (!videoId.isNullOrBlank()) {
            sponsorLayout.hide()
        } else if (videoVertical) {
            sponsorLayout.hide()
        } else {
            sponsorLayout.show()
            ImageHandler.loadImage2(sponsorImage, adsImageUrl, R.drawable.loading_page)
            viewModel?.let { infoViewModel ->
                sponsorImage.setOnClickListener {
                    listener.openRedirectUrl(generateLink(
                            infoViewModel.adsLink,
                            GroupChatAnalytics.ATTRIBUTE_BANNER,
                            infoViewModel.channelUrl,
                            infoViewModel.title))

                    analytics.eventClickBanner(infoViewModel, adsId, adsName, adsImageUrl)
                }
            }
        }

        if (sponsorLayout.visibility == View.VISIBLE) {
            viewModel?.run {
                analytics.eventViewBanner(this, adsId, adsName, adsImageUrl)
            }
        }
    }

    private fun generateLink(
            applink: String,
            attributeBanner: String,
            channelUrl: String,
            channelName: String): String {
        return if (applink.contains("?")) {
            applink + "&" + generateTrackerAttribution(attributeBanner,
                    channelUrl, channelName)
        } else {
            applink + "?" + generateTrackerAttribution(attributeBanner,
                    channelUrl, channelName)
        }
    }

    private fun generateTrackerAttribution(attributeBanner: String,
                                           channelUrl: String,
                                           channelName: String): String {
        return "tracker_attribution=" + GroupChatAnalytics.generateTrackerAttribution(attributeBanner, channelUrl, channelName)
    }

    fun getAttributionTracking(attributeName: String): String {
        return GroupChatAnalytics.generateTrackerAttribution(attributeName, viewModel?.channelUrl, viewModel?.title)
    }

    fun hideSponsor() {
        sponsorLayout.hide()
    }

    fun assignVideoVertical(videoVertical: Boolean) {
        this.videoVertical = videoVertical
        setSponsor()
    }
}