package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeRecommendationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeFeedContentGqlResponse>,
        private val homeRecommendationMapper: HomeRecommendationMapper
) : UseCase<HomeRecommendationDataModel>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeFeedContentGqlResponse::class.java)
    }

    override suspend fun executeOnBackground(): HomeRecommendationDataModel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        val tabName = params.getString(PARAM_TAB_NAME, "")
        val pageNumber = params.getInt(PARAM_PAGE, 0)
        return homeRecommendationMapper.mapToHomeRecommendationDataModel(graphqlUseCase.executeOnBackground(), tabName, pageNumber)
    }

    fun setParams(tabName: String, recomId: Int, count: Int, page: Int, location: String = "") {
        params.parameters.clear()
        params.putString(PARAM_TAB_NAME, tabName)
        params.putInt(PARAM_RECOM_ID, recomId)
        params.putInt(PARAM_COUNT, count)
        params.putInt(PARAM_PAGE, page)
        params.putString(PARAM_LOCATION, location)
    }

    companion object{
        private const val PARAM_TAB_NAME = "tabName"
        private const val PARAM_RECOM_ID = "recomID"
        private const val PARAM_COUNT = "count"
        private const val PARAM_PAGE = "page"
        private const val PARAM_LOCATION = "location"
    }
}