package com.tokopedia.videoTabComponent.di

import com.tokopedia.videoTabComponent.data.PlayVideoTabRepositoryImpl
import com.tokopedia.videoTabComponent.domain.PlayVideoTabRepository
import dagger.Binds
import dagger.Module

/**
 * Created by shruti agarwal on 24/11/22.
 */

@Module
abstract class PlayVideoTabRepositoryModule {
    @Binds
    abstract fun bindRepository(repository: PlayVideoTabRepositoryImpl): PlayVideoTabRepository
}
