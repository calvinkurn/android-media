package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeReviewSuggestedRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SuggestedProductReview>
): UseCase<SuggestedProductReview>(), HomeRepository<SuggestedProductReview> {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(SuggestedProductReview::class.java)
        graphqlUseCase.setRequestParams(mapOf())
    }
    override suspend fun executeOnBackground(): SuggestedProductReview {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): SuggestedProductReview {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): SuggestedProductReview {
        return SuggestedProductReview()
    }
}