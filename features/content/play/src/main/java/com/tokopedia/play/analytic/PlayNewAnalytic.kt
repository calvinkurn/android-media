package com.tokopedia.play.analytic

import com.tokopedia.play.analytic.campaign.PlayCampaignAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.tokonow.PlayTokoNowAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/07/22
 */
class PlayNewAnalytic @Inject constructor(
    partnerAnalytic: PlayPartnerAnalytic,
    likeAnalytic: PlayLikeAnalytic,
    socketAnalytic: PlaySocketAnalytic,
    upcomingAnalytic: PlayUpcomingAnalytic,
    shareExperienceAnalytic: PlayShareExperienceAnalytic,
    campaignAnalytic: PlayCampaignAnalytic,
    tokoNowAnalytic: PlayTokoNowAnalytic,
) : PlayPartnerAnalytic by partnerAnalytic,
    PlayLikeAnalytic by likeAnalytic,
    PlaySocketAnalytic by socketAnalytic,
    PlayUpcomingAnalytic by upcomingAnalytic,
    PlayShareExperienceAnalytic by shareExperienceAnalytic,
    PlayCampaignAnalytic by campaignAnalytic,
    PlayTokoNowAnalytic by tokoNowAnalytic {

    fun clickLihatToasterAtcPinnedProductCarousel(
        channelId: String,
        channelType: PlayChannelType,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            KEY_TRACK_CLICK_CONTENT,
            KEY_TRACK_GROUP_CHAT_ROOM,
            "click - pinned lihat keranjang",
            "$channelId - ${channelType.value}"
        )
    }
}