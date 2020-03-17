package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.pendingcashback.data.ResponsePendingCashback
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@Module
class HomeUseCaseModule {

    @HomeScope
    @Provides
    fun graphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @HomeScope
    @Provides
    fun provideGraphqlUseCase(graphqlRepository: GraphqlRepository) =
            com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>(graphqlRepository)

    @HomeScope
    @Provides
    fun homeUseCase(homeRepository: HomeRepository, homeDataMapper: HomeDataMapper) = HomeUseCase(homeRepository, homeDataMapper)

    @Provides
    fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository): SendGeolocationInfoUseCase {
        return SendGeolocationInfoUseCase(homeRepository)
    }

    @Provides
    fun provideGetHomeFeedUseCase(@ApplicationContext context: Context?,
                                  graphqlUseCase: GraphqlUseCase?,
                                  homeFeedMapper: HomeFeedMapper?): GetHomeFeedUseCase {
        return GetHomeFeedUseCase(context, graphqlUseCase, homeFeedMapper)
    }

    @Provides
    fun provideGetFeedTabUseCase(@ApplicationContext context: Context?,
                                 graphqlUseCase: GraphqlUseCase?,
                                 feedTabMapper: FeedTabMapper?): GetFeedTabUseCase {
        return GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper)
    }

    @Provides
    fun provideAddWishlistUseCase(@ApplicationContext context: Context?): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @Provides
    fun provideRemoveWishListUseCase(@ApplicationContext context: Context?): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @Provides
    @HomeScope
    fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): StickyLoginUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.stickylogin.R.raw.gql_sticky_login_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<StickyLoginTickerPojo.TickerResponse>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return StickyLoginUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.suggested_review_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<SuggestedProductReview>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetHomeReviewSuggestedUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.dismiss_suggested_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ProductrevDismissSuggestion>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return DismissHomeReviewUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsDataUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetHomeTokopointsDataUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.home_gql_tokopoints_details)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TokopointsDrawerHomeData>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetHomeTokopointsDataUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideGetPlayLiveDynamicDataUseCase(graphqlRepository: GraphqlRepository): GetPlayLiveDynamicUseCase {
        return GetPlayLiveDynamicUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(graphqlRepository: GraphqlRepository): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getCoroutineWalletBalanceUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, userSession: UserSessionInterface, remoteConfig: RemoteConfig, localCacheHandler: LocalCacheHandler): GetCoroutineWalletBalanceUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.common_wallet.R.raw.wallet_balance_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }

    @HomeScope
    @Provides
    fun getGetCoroutinePendingCashbackUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetCoroutinePendingCashbackUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, com.tokopedia.common_wallet.R.raw.wallet_pending_cashback_query)
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ResponsePendingCashback>(graphqlRepository)
        usecase.setGraphqlQuery(query)
        return GetCoroutinePendingCashbackUseCase(usecase)
    }

    @Provides
    @HomeScope
    fun provideItemTabBusinessViewModel(graphqlUseCase: GraphqlUseCase?): ItemTabBusinessViewModel = ItemTabBusinessViewModel(graphqlUseCase!!)

    @Provides
    @HomeScope
    fun providePopularKeywordUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>): GetPopularKeywordUseCase {
        return GetPopularKeywordUseCase(graphqlUseCase as com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeWidget.PopularKeywordQuery>)

    }
}