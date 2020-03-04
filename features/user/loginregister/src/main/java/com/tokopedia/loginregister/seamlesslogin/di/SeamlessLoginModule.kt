package com.tokopedia.loginregister.seamlesslogin.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by nisie on 10/25/18.
 */
@Module
class SeamlessLoginModule {
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SeamlessLoginScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @SeamlessLoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

//    @SeamlessLoginScope
//    @Provides
//    fun provideSeamlessLoginViewModel(userSessionInterface: UserSessionInterface, dispatcher: CoroutineDispatcher)
//            : SeamlessLoginViewModel = SeamlessLoginViewModel(userSessionInterface, dispatcher)

}
