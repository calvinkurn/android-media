package com.tokopedia.play.di

import com.tokopedia.play.data.PlayViewerInteractiveRepositoryImpl
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
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
}