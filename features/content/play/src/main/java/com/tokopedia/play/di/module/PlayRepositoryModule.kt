package com.tokopedia.play.di.module

import com.tokopedia.play.data.repository.*
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.domain.repository.*
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorageImpl
import dagger.Binds
import dagger.Module

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
@Module
abstract class PlayRepositoryModule {

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
    abstract fun bindCartRepository(repo: PlayViewerChannelRepositoryImpl): PlayViewerChannelRepository

    @Binds
    @PlayScope
    abstract fun bindChannelRepository(repo: PlayViewerCartRepositoryImpl): PlayViewerCartRepository

    @Binds
    @PlayScope
    abstract fun bindTagItemRepository(repo: PlayViewerTagItemRepositoryImpl): PlayViewerTagItemRepository

    @Binds
    @PlayScope
    abstract fun bindBroTrackerRepository(repo: PlayViewerBroTrackerRepositoryImpl): PlayViewerBroTrackerRepository

    @Binds
    @PlayScope
    abstract fun bindUserReportRepository(repo: PlayViewerUserReportRepositoryImpl): PlayViewerUserReportRepository

    @Binds
    @PlayScope
    abstract fun bindSocketRepository(repo: PlayViewerSocketRepositoryImpl): PlayViewerSocketRepository

    @Binds
    @PlayScope
    abstract fun bindRepository(repo: PlayViewerRepositoryImpl): PlayViewerRepository

    @Binds
    @PlayScope
    abstract fun bindWidgetRepository(repo: PlayExploreWidgetRepositoryImpl) : PlayExploreWidgetRepository
}
