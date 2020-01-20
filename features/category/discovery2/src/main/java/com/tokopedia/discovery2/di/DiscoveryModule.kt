package com.tokopedia.discovery2.di

import com.tokopedia.discovery.categoryrevamp.di.DiscoveryScope
import com.tokopedia.tradein_common.repository.BaseRepository
import dagger.Module
import dagger.Provides

@Module
class DiscoveryModule {

    @DiscoveryScope
    @Provides
    fun provideBaseRepository() : BaseRepository{
        return BaseRepository.repositoryInstance
    }
}