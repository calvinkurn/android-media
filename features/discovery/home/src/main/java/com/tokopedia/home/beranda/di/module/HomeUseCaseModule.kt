package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
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
    fun provideStickyLoginUseCase(graphqlRepository: GraphqlRepository): StickyLoginUseCase {
        return StickyLoginUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @Provides
    @HomeScope
    fun provideHomeReviewSuggestedUseCase(graphqlRepository: GraphqlRepository): GetHomeReviewSuggestedUseCase {
        return GetHomeReviewSuggestedUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @Provides
    @HomeScope
    fun provideDismissHomeReviewUseCase(graphqlRepository: GraphqlRepository): DismissHomeReviewUseCase {
        return DismissHomeReviewUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @Provides
    @HomeScope
    fun provideHomeTokopointsDataUseCase(graphqlRepository: GraphqlRepository): GetHomeTokopointsDataUseCase {
        return GetHomeTokopointsDataUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @Provides
    @HomeScope
    fun provideGetPlayLiveDynamicDataUseCase(graphqlRepository: GraphqlRepository): GetPlayLiveDynamicUseCase {
        return GetPlayLiveDynamicUseCase(com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase(graphqlRepository))
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(graphqlUseCase as com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<KeywordSearchData> )
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