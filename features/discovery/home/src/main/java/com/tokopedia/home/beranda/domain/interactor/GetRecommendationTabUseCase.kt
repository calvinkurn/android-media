package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel
import com.tokopedia.usecase.coroutines.UseCase

class GetRecommendationTabUseCase(
        private val graphqlUseCase: GraphqlUseCase<HomeFeedTabGqlResponse>
) : UseCase<List<FeedTabModel>>(){
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeFeedTabGqlResponse::class.java)
    }
    override suspend fun executeOnBackground(): List<FeedTabModel> {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground().homeRecommendation.recommendationTabs.withIndex().map { pair ->
            FeedTabModel(
                    id = pair.value.id,
                    name = pair.value.name,
                    imageUrl = pair.value.imageUrl,
                    position = pair.index + 1
            )
        }
    }

}