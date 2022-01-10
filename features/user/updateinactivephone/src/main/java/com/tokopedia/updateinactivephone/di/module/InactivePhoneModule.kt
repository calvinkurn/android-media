package com.tokopedia.updateinactivephone.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApiClient
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class InactivePhoneModule {

    @ActivityScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ActivityScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun provideApiClient(): InactivePhoneApiClient<InactivePhoneApi> {
        return InactivePhoneApiClient(InactivePhoneApi::class.java)
    }
}