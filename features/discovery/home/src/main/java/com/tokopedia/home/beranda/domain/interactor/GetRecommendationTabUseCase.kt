package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.di.module.query.RecommendationTabV2Query
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetRecommendationTabUseCase(
    private val graphqlUseCase: GraphqlUseCase<HomeFeedTabGqlResponse>,
) : UseCase<List<RecommendationTabDataModel>>() {
    init {
        graphqlUseCase.setGraphqlQuery(RecommendationTabV2Query())
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeFeedTabGqlResponse::class.java)
    }
    override suspend fun executeOnBackground(): List<RecommendationTabDataModel> {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground().homeRecommendation.recommendationTabs.withIndex().map { pair ->
            RecommendationTabDataModel(
                id = pair.value.id,
                name = pair.value.name,
                imageUrl = pair.value.imageUrl,
                position = pair.index + 1,
                sourceType = pair.value.sourceType,
                isJumperTab = pair.value.isJumperTab
            )
        }
    }

    fun setParams(location: String = "", page: String = "") {
        val requestParams: RequestParams = RequestParams.EMPTY
        requestParams.putString(LOCATION, location)
        requestParams.putString(PAGE, page)
        graphqlUseCase.setRequestParams(requestParams.parameters)
    }

    companion object {
        private const val LOCATION = "location"
        private const val PAGE = "page"
    }
}
