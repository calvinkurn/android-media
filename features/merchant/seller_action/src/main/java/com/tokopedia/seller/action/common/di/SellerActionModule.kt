package com.tokopedia.seller.action.common.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
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
class SellerActionModule(private val context: Context) {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        GraphqlClient.init(context.applicationContext)
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
    fun providerUserSession(): UserSessionInterface = UserSession(context)

    @SellerActionScope
    @Provides
    fun provideSellerActionPresenter(
            sliceMainOrderListUseCase: SliceMainOrderListUseCase,
            sliceReviewStarsUseCase: SliceReviewStarsUseCase,
            dispatcher: SellerActionDispatcherProvider): SliceSellerActionPresenter {
        return SliceSellerActionPresenterImpl(sliceMainOrderListUseCase, sliceReviewStarsUseCase, dispatcher)
    }

}