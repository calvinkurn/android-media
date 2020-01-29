package com.tokopedia.discovery2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery2.repository.pushstatus.PushStatusGQLRepository
import com.tokopedia.discovery2.repository.pushstatus.PushStatusRepository
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

    @DiscoveryScope
    @Provides
    fun providePushStatusGQLRepository(@ApplicationContext context: Context): PushStatusRepository {
        return PushStatusGQLRepository(provideGetStringMethod(context))
    }


    @DiscoveryScope
    @Provides
    fun provideGetStringMethod(@ApplicationContext context: Context): (Int) -> String {
        return {id -> GraphqlHelper.loadRawString(context.resources,id)};
    }
}