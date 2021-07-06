package com.tokopedia.play.di

import com.tokopedia.play.data.repository.PlayViewerInteractiveRepositoryImpl
import com.tokopedia.play.data.repository.PlayViewerPartnerRepositoryImpl
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
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
}