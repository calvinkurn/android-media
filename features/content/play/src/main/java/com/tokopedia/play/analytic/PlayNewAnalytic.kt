package com.tokopedia.play.analytic

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.play.analytic.campaign.PlayCampaignAnalytic
import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.popup.PlayFollowPopupAnalytic
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.tokonow.PlayTokoNowAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
import com.tokopedia.play.analytic.voucher.PlayVoucherAnalytic
import com.tokopedia.play.view.type.PlayChannelType
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
    playFollowPopupAnalytic: PlayFollowPopupAnalytic
) : PlayPartnerAnalytic by partnerAnalytic,
    PlayLikeAnalytic by likeAnalytic,
    PlaySocketAnalytic by socketAnalytic,
    PlayUpcomingAnalytic by upcomingAnalytic,
    PlayShareExperienceAnalytic by shareExperienceAnalytic,
    PlayCampaignAnalytic by campaignAnalytic,
    PlayInteractiveAnalytic by interactiveAnalytic,
    PlayTokoNowAnalytic by tokoNowAnalytic,
    PlayVoucherAnalytic by playVoucherAnalytic,
    PlayFollowPopupAnalytic by playFollowPopupAnalytic {

    fun clickLihatToasterAtcPinnedProductCarousel(
        channelId: String,
        channelType: PlayChannelType
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - pinned lihat keranjang")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - ${channelType.value}")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }
}
