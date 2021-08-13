package com.tokopedia.seller.action.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.action.common.analytics.SellerActionAnalytics
import com.tokopedia.seller.action.common.analytics.SellerActionAnalyticsImpl
import com.tokopedia.seller.action.common.presentation.presenter.SellerActionPresenter
import com.tokopedia.seller.action.common.provider.SellerActionProvider
import com.tokopedia.seller.action.common.provider.SellerActionProviderImpl
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class SellerActionModule {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerActionScope
    @Provides
    fun providerUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerActionScope
    @Provides
    fun provideFirebaseRemoteConfigImpl(@ApplicationContext context: Context): FirebaseRemoteConfigImpl = FirebaseRemoteConfigImpl(context)

    @SellerActionScope
    @Provides
    fun provideSellerActionAnalytics(userSession: UserSessionInterface): SellerActionAnalytics = SellerActionAnalyticsImpl(userSession)

    @SellerActionScope
    @Provides
    fun providerSellerActionProvider(): SellerActionProvider = SellerActionProviderImpl()

    @SellerActionScope
    @Provides
    fun providePresenter(sliceMainOrderListUseCase: SliceMainOrderListUseCase,
                         dispatcher: CoroutineDispatchers,
                         provider: SellerActionProvider): SellerActionPresenter = SellerActionPresenter(sliceMainOrderListUseCase, dispatcher, provider)

}