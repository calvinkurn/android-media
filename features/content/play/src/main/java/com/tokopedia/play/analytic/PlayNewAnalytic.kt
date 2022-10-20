package com.tokopedia.play.analytic

import com.tokopedia.play.analytic.campaign.PlayCampaignAnalytic
import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.tokonow.PlayTokoNowAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
import com.tokopedia.play.analytic.voucher.PlayVoucherAnalytic
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_SCREEN_NAME
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_TRACKER_ID
import com.tokopedia.track.builder.Tracker
import javax.inject.Inject

/**
 * Created by jegul on 09/07/21
 */
class PlayNewAnalytic @Inject constructor(
        partnerAnalytic: PlayPartnerAnalytic,
        likeAnalytic: PlayLikeAnalytic,
        socketAnalytic: PlaySocketAnalytic,
        upcomingAnalytic: PlayUpcomingAnalytic,
        shareExperienceAnalytic: PlayShareExperienceAnalytic,
        campaignAnalytic: PlayCampaignAnalytic,
        interactiveAnalytic: PlayInteractiveAnalytic,
        tokoNowAnalytic: PlayTokoNowAnalytic,
        playVoucherAnalytic: PlayVoucherAnalytic,
) : PlayPartnerAnalytic by partnerAnalytic,
        PlayLikeAnalytic by likeAnalytic,
        PlaySocketAnalytic by socketAnalytic,
        PlayUpcomingAnalytic by upcomingAnalytic,
        PlayShareExperienceAnalytic by shareExperienceAnalytic,
        PlayCampaignAnalytic by campaignAnalytic,
        PlayInteractiveAnalytic by interactiveAnalytic,
        PlayTokoNowAnalytic by tokoNowAnalytic,
        PlayVoucherAnalytic by playVoucherAnalytic {

        fun clickLihatToasterAtcPinnedProductCarousel(
                channelId: String,
                channelType: PlayChannelType,
        ) {
                Tracker.Builder()
                        .setEvent(KEY_TRACK_CLICK_CONTENT)
                        .setEventAction("click - pinned lihat keranjang")
                        .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
                        .setEventLabel("$channelId - ${channelType.value}")
                        .setBusinessUnit(VAL_BUSINESS_UNIT)
                        .setCurrentSite(VAL_CURRENT_SITE)
                        .build()
                        .send()
        }

        fun screenWithSwipeCoachMark(isShown: Boolean, isLoggedIn: Boolean, channelType: PlayChannelType, userId: String, channelId: String) {
            Tracker.Builder()
                .setEvent("openScreen")
                .setCustomProperty(KEY_TRACKER_ID, "13881")
                .setBusinessUnit(VAL_BUSINESS_UNIT)
                .setCurrentSite(VAL_CURRENT_SITE)
                .setCustomProperty(KEY_IS_LOGGED_IN_STATUS, isLoggedIn.toString())
                .setCustomProperty(KEY_SCREEN_NAME, "/group-chat-room/$channelId/${channelType.value}/is coachmark $isShown")
                .setUserId(userId)
                .build()
                .send()
        }
}
