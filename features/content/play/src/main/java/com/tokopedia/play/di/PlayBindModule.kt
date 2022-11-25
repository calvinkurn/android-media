package com.tokopedia.play.di

import com.tokopedia.play.analytic.campaign.PlayCampaignAnalytic
import com.tokopedia.play.analytic.campaign.PlayCampaignAnalyticImpl
import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalyticImpl
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalyticImpl
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalyticImpl
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalytic
import com.tokopedia.play.analytic.share.PlayShareExperienceAnalyticImpl
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalyticImpl
import com.tokopedia.play.analytic.tagitem.PlayTagItemsAnalytic
import com.tokopedia.play.analytic.tagitem.PlayTagItemsAnalyticImpl
import com.tokopedia.play.analytic.tokonow.PlayTokoNowAnalytic
import com.tokopedia.play.analytic.tokonow.PlayTokoNowAnalyticImpl
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalyticImpl
import com.tokopedia.play.analytic.voucher.PlayVoucherAnalytic
import com.tokopedia.play.analytic.voucher.PlayVoucherAnalyticImpl
import com.tokopedia.play.util.logger.PlayLog
import com.tokopedia.play.util.logger.PlayLogImpl
import com.tokopedia.play.util.timer.PlayTimerFactory
import com.tokopedia.play.util.timer.TimerFactory
import dagger.Binds
import dagger.Module

/**
 * Created by jegul on 30/06/21
 */
@Module
abstract class PlayBindModule {

    /**
     * Analytic
     */
    @Binds
    @PlayScope
    abstract fun bindPartnerAnalytic(analytic: PlayPartnerAnalyticImpl): PlayPartnerAnalytic

    @Binds
    @PlayScope
    abstract fun bindLikeAnalytic(analytic: PlayLikeAnalyticImpl): PlayLikeAnalytic

    @Binds
    @PlayScope
    abstract fun bindSocketAnalytic(analytic: PlaySocketAnalyticImpl): PlaySocketAnalytic

    @Binds
    @PlayScope
    abstract fun bindUpcomingAnalytic(analytic: PlayUpcomingAnalyticImpl): PlayUpcomingAnalytic

    @Binds
    @PlayScope
    abstract fun bindShareExperienceAnalytic(analytic: PlayShareExperienceAnalyticImpl): PlayShareExperienceAnalytic

    @Binds
    @PlayScope
    abstract fun bindCampaignAnalytic(analytic: PlayCampaignAnalyticImpl): PlayCampaignAnalytic

    @Binds
    @PlayScope
    abstract fun bindInteractiveAnalytic(analytic: PlayInteractiveAnalyticImpl): PlayInteractiveAnalytic

    @Binds
    @PlayScope
    abstract fun bindTagItemsAnalyticFactory(factory: PlayTagItemsAnalyticImpl.Factory): PlayTagItemsAnalytic.Factory

    @Binds
    @PlayScope
    abstract fun bindTokonowAnalytic(analytic: PlayTokoNowAnalyticImpl): PlayTokoNowAnalytic

    @Binds
    @PlayScope
    abstract fun bindVoucherAnalytic(analytic: PlayVoucherAnalyticImpl): PlayVoucherAnalytic

    /**
     * Utils
     */
    @Binds
    @PlayScope
    abstract fun bindTimerFactory(timerFactory: PlayTimerFactory): TimerFactory

    @Binds
    @PlayScope
    abstract fun bindPlayLog(log: PlayLogImpl): PlayLog
}