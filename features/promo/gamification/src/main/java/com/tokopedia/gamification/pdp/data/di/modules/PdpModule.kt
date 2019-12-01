package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.domain.Mapper
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [TopAdsWishlistModule::class])
class PdpModule {

    @GamificationPdpScope
    @Provides
    fun getGamingRecommendationProductUseCase(@Named(GqlQueryModule.RECOMMENDATION_QUERY) qeury: String,
                                              graphqlUseCase: GraphqlUseCase,
                                              userSession: UserSessionInterface,
                                              mapper: Mapper): GamingRecommendationProductUseCase {
        return GamingRecommendationProductUseCase(qeury, graphqlUseCase, userSession, mapper)
    }

    @GamificationPdpScope
    @Provides
    fun provideAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @GamificationPdpScope
    @Provides
    fun provideRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase{
        return RemoveWishListUseCase(context)
    }

//    @GamificationPdpScope
//    @Provides
//    fun provideTopAdsWishlishedUseCase(context: Context): TopAdsWishlishedUseCase {
//        return TopAdsWishlishedUseCase(context)
//    }

    @GamificationPdpScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @GamificationPdpScope
    @Provides
    fun provideMapper(): Mapper {
        return Mapper()
    }


}