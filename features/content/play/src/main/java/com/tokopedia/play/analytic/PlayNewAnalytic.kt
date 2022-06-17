package com.tokopedia.play.analytic

import com.tokopedia.play.analytic.campaign.PlayCampaignAnalytic
import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
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
) : PlayPartnerAnalytic by partnerAnalytic,
        PlayLikeAnalytic by likeAnalytic,
        PlaySocketAnalytic by socketAnalytic,
        PlayUpcomingAnalytic by upcomingAnalytic,
        PlayShareExperienceAnalytic by shareExperienceAnalytic,
        PlayCampaignAnalytic by campaignAnalytic,
        PlayInteractiveAnalytic by interactiveAnalytic