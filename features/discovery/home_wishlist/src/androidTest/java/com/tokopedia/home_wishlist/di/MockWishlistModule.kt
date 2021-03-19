package com.tokopedia.home_wishlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import io.mockk.mockk

class MockWishlistModule : WishlistModule() {
    override fun provideGetSingleRecommendationUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetSingleRecommendationUseCase {
        return mockk(relaxed = true)
    }
}