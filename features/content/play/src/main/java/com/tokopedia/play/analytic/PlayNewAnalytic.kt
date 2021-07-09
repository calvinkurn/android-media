package com.tokopedia.play.analytic

import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import javax.inject.Inject

/**
 * Created by jegul on 09/07/21
 */
class PlayNewAnalytic @Inject constructor(
        partnerAnalytic: PlayPartnerAnalytic,
        interactiveAnalytic: PlayInteractiveAnalytic
) : PlayPartnerAnalytic by partnerAnalytic, PlayInteractiveAnalytic by interactiveAnalytic