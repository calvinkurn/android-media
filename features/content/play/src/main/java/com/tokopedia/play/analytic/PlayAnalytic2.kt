package com.tokopedia.play.analytic

import com.tokopedia.play.analytic.kebab.PlayKebabAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.tagitem.PlayTagItemsAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by kenny.hadisaputra on 11/03/22
 */
class PlayAnalytic2 @AssistedInject constructor(
    @Assisted trackingQueue: TrackingQueue,
    @Assisted channelInfo: PlayChannelInfoUiModel,
    partnerAnalytic: PlayPartnerAnalytic,
    likeAnalytic: PlayLikeAnalytic,
    socketAnalytic: PlaySocketAnalytic,
    upcomingAnalytic: PlayUpcomingAnalytic,
    shareExperienceAnalytic: PlayShareExperienceAnalytic,
    tagItemsAnalytic: PlayTagItemsAnalytic.Factory,
    kebabAnalytic: PlayKebabAnalytic.Factory,
) : PlayPartnerAnalytic by partnerAnalytic,
    PlayLikeAnalytic by likeAnalytic,
    PlaySocketAnalytic by socketAnalytic,
    PlayUpcomingAnalytic by upcomingAnalytic,
    PlayShareExperienceAnalytic by shareExperienceAnalytic,
    PlayTagItemsAnalytic by tagItemsAnalytic.create(trackingQueue, channelInfo),
    PlayKebabAnalytic by kebabAnalytic.create(channelInfo) {

    @AssistedFactory
    interface Factory {
        fun create(
            trackingQueue: TrackingQueue,
            channelInfo: PlayChannelInfoUiModel,
        ): PlayAnalytic2
    }
}
