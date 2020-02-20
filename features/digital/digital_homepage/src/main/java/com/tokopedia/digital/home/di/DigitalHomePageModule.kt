package com.tokopedia.digital.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.home.domain.GetSortListHomePageUseCase
import com.tokopedia.digital.home.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.presentation.Util.DigitalHomeTrackingUtil
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DigitalHomePageModule {

    @DigitalHomePageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @DigitalHomePageScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @DigitalHomePageScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DigitalHomePageScope
    @Provides
    fun provideGetEmptyVMsUseCase(): GetSortListHomePageUseCase = GetSortListHomePageUseCase()

    @DigitalHomePageScope
    @Provides
    fun provideSearchCategoryUseCase(graphqlRepository: GraphqlRepository): SearchCategoryHomePageUseCase =
            SearchCategoryHomePageUseCase(graphqlRepository)

    @DigitalHomePageScope
    @Provides
    fun provideDigitalHomepageTracking(): DigitalHomeTrackingUtil = DigitalHomeTrackingUtil()

}
