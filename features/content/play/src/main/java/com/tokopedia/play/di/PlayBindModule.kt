package com.tokopedia.play.di

import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalytic
import com.tokopedia.play.analytic.interactive.PlayInteractiveAnalyticImpl
import com.tokopedia.play.analytic.like.PlayLikeAnalytic
import com.tokopedia.play.analytic.like.PlayLikeAnalyticImpl
import com.tokopedia.play.analytic.partner.PlayPartnerAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalyticImpl
import com.tokopedia.play.analytic.socket.PlaySocketAnalytic
import com.tokopedia.play.analytic.socket.PlaySocketAnalyticImpl
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalytic
import com.tokopedia.play.analytic.upcoming.PlayUpcomingAnalyticImpl
import com.tokopedia.play.data.repository.*
import com.tokopedia.play.domain.repository.*
import com.tokopedia.play.util.timer.PlayTimerFactory
import com.tokopedia.play.util.timer.TimerFactory
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorageImpl
import dagger.Binds
import dagger.Module

/**
 * Created by jegul on 30/06/21
 */
@Module
abstract class PlayBindModule {

    @Binds
    @PlayScope
    abstract fun bindInteractiveRepository(repo: PlayViewerInteractiveRepositoryImpl): PlayViewerInteractiveRepository

    @Binds
    @PlayScope
    abstract fun bindInteractiveStorage(storage: PlayInteractiveStorageImpl): PlayInteractiveStorage

    @Binds
    @PlayScope
    abstract fun bindPartnerRepository(repo: PlayViewerPartnerRepositoryImpl): PlayViewerPartnerRepository

    @Binds
    @PlayScope
    abstract fun bindLikeRepository(repo: PlayViewerLikeRepositoryImpl): PlayViewerLikeRepository

    @Binds
    @PlayScope
    abstract fun bindCartRepository(repo: PlayViewerCartRepositoryImpl): PlayViewerCartRepository

    @Binds
    @PlayScope
    abstract fun bindUserReportRepository(repo: PlayViewerUserReportRepositoryImpl): PlayViewerUserReportRepository

    @Binds
    @PlayScope
    abstract fun bindRepository(repo: PlayViewerRepositoryImpl): PlayViewerRepository

    /**
     * Analytic
     */
    @Binds
    @PlayScope
    abstract fun bindPartnerAnalytic(analytic: PlayPartnerAnalyticImpl): PlayPartnerAnalytic

    @Binds
    @PlayScope
    abstract fun bindInteractiveAnalytic(analytic: PlayInteractiveAnalyticImpl): PlayInteractiveAnalytic

    @Binds
    @PlayScope
    abstract fun bindLikeAnalytic(analytic: PlayLikeAnalyticImpl): PlayLikeAnalytic

    @Binds
    @PlayScope
    abstract fun bindSocketAnalytic(analytic: PlaySocketAnalyticImpl): PlaySocketAnalytic

    @Binds
    @PlayScope
    abstract fun bindUpcomingAnalytic(analytic: PlayUpcomingAnalyticImpl): PlayUpcomingAnalytic

    /**
     * Utils
     */
    @Binds
    @PlayScope
    abstract fun bindTimerFactory(timerFactory: PlayTimerFactory): TimerFactory
}