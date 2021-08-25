package com.tokopedia.play.analytic

import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import javax.inject.Inject

/**
 * Created by jegul on 09/07/21
 */
class PlayNewAnalytic @Inject constructor(
        partnerAnalytic: PlayPartnerAnalytic,
        interactiveAnalytic: PlayInteractiveAnalytic,
        likeAnalytic: PlayLikeAnalytic,
        socketAnalytic: PlaySocketAnalytic,
) : PlayPartnerAnalytic by partnerAnalytic,
        PlayInteractiveAnalytic by interactiveAnalytic,
        PlayLikeAnalytic by likeAnalytic,
        PlaySocketAnalytic by socketAnalytic