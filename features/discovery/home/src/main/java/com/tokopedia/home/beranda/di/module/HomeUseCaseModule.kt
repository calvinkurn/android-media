package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.GetFeedTabUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.domain.interactor.GetKeywordSearchUseCase
import com.tokopedia.home.beranda.domain.interactor.SendGeolocationInfoUseCase
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
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
    fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository?): StickyLoginUseCase {
        return StickyLoginUseCase(context.resources, graphqlRepository!!)
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(homeRepository: HomeRepository): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(homeRepository)
    }


}