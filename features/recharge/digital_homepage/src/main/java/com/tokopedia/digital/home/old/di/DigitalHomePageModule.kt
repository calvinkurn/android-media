package com.tokopedia.digital.home.old.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.home.old.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.old.domain.SearchCategoryHomePageUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.digital.home.old.presentation.util.DigitalHomeTrackingUtil
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class DigitalHomePageModule {

    @DigitalHomePageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @DigitalHomePageScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @DigitalHomePageScope
    @Provides
    fun provideDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @DigitalHomePageScope
    @Provides
    fun provideDigitalHomePageUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase): DigitalHomePageUseCase =
            DigitalHomePageUseCase(multiRequestGraphqlUseCase)

    @DigitalHomePageScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @DigitalHomePageScope
    @Provides
    fun provideSearchCategoryUseCase(graphqlRepository: GraphqlRepository): SearchCategoryHomePageUseCase =
            SearchCategoryHomePageUseCase(graphqlRepository)

    @DigitalHomePageScope
    @Provides
    fun provideDigitalHomepageTracking(): DigitalHomeTrackingUtil = DigitalHomeTrackingUtil()

}
