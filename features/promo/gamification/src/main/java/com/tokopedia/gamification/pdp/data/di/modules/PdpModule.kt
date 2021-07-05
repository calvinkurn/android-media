package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.domain.Mapper
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [TopAdsWishlistModule::class,ActivityContextModule::class])
class PdpModule {

    @GamificationPdpScope
    @Provides
    fun getGamingRecommendationProductUseCase(context: Context,
                                              graphqlUseCase: GraphqlUseCase,
                                              userSession: UserSessionInterface,
                                              mapper: Mapper): GamingRecommendationProductUseCase {
        return GamingRecommendationProductUseCase(context, graphqlUseCase, userSession, mapper)
    }

    @GamificationPdpScope
    @Provides
    fun provideAddWishListUseCase(context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @GamificationPdpScope
    @Provides
    fun provideRemoveWishListUseCase(context: Context): RemoveWishListUseCase{
        return RemoveWishListUseCase(context)
    }

    @GamificationPdpScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @GamificationPdpScope
    @Provides
    fun provideMapper(): Mapper {
        return Mapper()
    }


}