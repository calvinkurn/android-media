package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.mapper.HomeGlobalRecommendationMapper
import com.tokopedia.home.beranda.di.module.query.HomeFeedV2Query
import com.tokopedia.home.beranda.domain.gql.feed.GetHomeRecommendationContent
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.RecomTemporary
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@RecomTemporary
class GetHomeGlobalRecommendationUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetHomeRecommendationContent>,
    private val homeRecommendationMapper: HomeGlobalRecommendationMapper
) {
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(HomeFeedV2Query())
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(GetHomeRecommendationContent::class.java)
    }

    suspend fun executeOnBackground(): HomeGlobalRecommendationDataModel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        val tabName = params.getString(PARAM_TAB_NAME, "")
        val pageNumber = params.getInt(PARAM_PRODUCT_PAGE, 0)
        val response = graphqlUseCase.executeOnBackground()
        val map = homeRecommendationMapper.mapToHomeRecommendationDataModel(response, tabName, pageNumber)
        return map
    }

    fun setParams(tabName: String, recomId: Int, count: Int, page: Int, location: String, sourceType: String) {
        params.parameters.clear()
        params.putString(PARAM_TAB_NAME, tabName)
        params.putString(PARAM_LOCATION, location)
        params.putString(PARAM_SOURCE_TYPE, sourceType)
        params.putInt(PARAM_PRODUCT_PAGE, page)
        params.putString(PRODUCT_CARD_VERSION, getProductCardVersion())
    }

    private fun getProductCardVersion(): String {
        val isReimagine = ProductCardExperiment.isReimagine()
        return if(isReimagine) PRODUCT_CARD_VERSION_V5 else ""
    }

    companion object {
        private const val PARAM_TAB_NAME = "tabName"
        private const val PARAM_LOCATION = "location"
        private const val PARAM_SOURCE_TYPE = "sourceType"
        private const val PARAM_PRODUCT_PAGE = "productPage"
        private const val PRODUCT_CARD_VERSION = "productCardVersion"
        private const val PRODUCT_CARD_VERSION_V5 = "v5"
    }
}
