package com.tokopedia.checkout.view.feature.emptycart2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

@Module
@EmptyCartScope
class EmptyCartModule {

    @Provides
    @EmptyCartScope
    fun providesGetWishListUseCase(@ApplicationContext context: Context): GetWishlistUseCase {
        return GetWishlistUseCase(context)
    }

    @Provides
    @EmptyCartScope
    fun providesGetRecentViewUseCase(@ApplicationContext context: Context): GetRecentViewUseCase {
        return GetRecentViewUseCase(context)
    }

    @Provides
    @EmptyCartScope
    fun providesUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @EmptyCartScope
    fun providesGraphqlUseCase(@ApplicationContext context: Context): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @EmptyCartScope
    fun providesGetRecommendationUseCase(@ApplicationContext context: Context,
                                         userSession: UserSessionInterface,
                                         graphqlUseCase: GraphqlUseCase): GetRecommendationUseCase {
        return GetRecommendationUseCase(context, graphqlUseCase, userSession)
    }

}