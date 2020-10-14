package com.tokopedia.seller.action.common.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.action.balance.domain.usecase.SliceSellerBalanceUseCase
import com.tokopedia.seller.action.balance.domain.usecase.SliceTopadsBalanceUseCase
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcher
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.presentation.presenter.SliceSellerActionPresenter
import com.tokopedia.seller.action.common.presentation.presenter.SliceSellerActionPresenterImpl
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.seller.action.review.domain.usecase.SliceReviewStarsUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@SellerActionScope
@Module
class SellerActionModule() {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository(@ApplicationContext context: Context): GraphqlRepository {
        GraphqlClient.init(context)
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerActionScope
    @Provides
    fun provideDispatchers(): SellerActionDispatcherProvider = SellerActionDispatcher

    @SellerActionScope
    @Provides
    fun providerHandler(): Handler = Handler(Looper.getMainLooper())

    @SellerActionScope
    @Provides
    fun providerUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerActionScope
    @Provides
    fun provideSellerActionPresenter(
            sliceMainOrderListUseCase: SliceMainOrderListUseCase,
            sliceReviewStarsUseCase: SliceReviewStarsUseCase,
            sliceSellerBalanceUseCase: SliceSellerBalanceUseCase,
            sliceTopadsBalanceUseCase: SliceTopadsBalanceUseCase,
            dispatcher: SellerActionDispatcherProvider): SliceSellerActionPresenter {
        return SliceSellerActionPresenterImpl(sliceMainOrderListUseCase, sliceReviewStarsUseCase, sliceSellerBalanceUseCase, sliceTopadsBalanceUseCase, dispatcher)
    }

    @SellerActionScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl = FirebaseRemoteConfigImpl(context)

}