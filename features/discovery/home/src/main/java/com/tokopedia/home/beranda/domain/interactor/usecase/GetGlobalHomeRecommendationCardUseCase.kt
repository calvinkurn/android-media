package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.beranda.data.mapper.HomeGlobalRecommendationCardMapper
import com.tokopedia.home.beranda.domain.gql.query.GetHomeRecommendationCardQuery
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import com.tokopedia.recommendation_widget_common.byteio.RefreshType
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetGlobalHomeRecommendationCardUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetHomeRecommendationCardResponse>,
    private val homeRecommendationCardMapper: HomeGlobalRecommendationCardMapper
) {

    init {
        graphqlUseCase.setGraphqlQuery(GetHomeRecommendationCardQuery())
        graphqlUseCase.setTypeClass(GetHomeRecommendationCardResponse::class.java)
    }

    suspend fun execute(
        productPage: Int,
        tabIndex: Int,
        tabName: String,
        paramSource: String,
        location: String,
        refreshType: RefreshType,
        bytedanceSessionId: String,
        currentTotalData: Int = 0,
    ): HomeGlobalRecommendationDataModel {
        graphqlUseCase.setRequestParams(createRequestParams(productPage, paramSource, location, refreshType, bytedanceSessionId))
        return homeRecommendationCardMapper.mapToRecommendationCardDataModel(
            graphqlUseCase.executeOnBackground().getHomeRecommendationCard,
            tabIndex,
            tabName,
            productPage,
            currentTotalData
        )
    }

    private fun createRequestParams(
        productPage: Int,
        paramSource: String,
        location: String,
        refreshType: RefreshType,
        bytedanceSessionId: String,
    ): Map<String, Any> {
        return RequestParams.create().apply {
            putInt(PARAM_PRODUCT_PAGE, productPage)
            putString(PARAM_LAYOUTS, LAYOUTS_VALUE)
            putString(PARAM_SOURCE_TYPE, paramSource)
            putString(PARAM_LOCATION, location)
            putString(PRODUCT_CARD_VERSION, PRODUCT_CARD_VERSION_V5)
            putInt(REFRESH_TYPE, refreshType.value)
            putString(BYTEDANCE_SESSION_ID, bytedanceSessionId)
        }.parameters
    }

    companion object {
        private const val PARAM_LOCATION = "location"
        private const val PARAM_SOURCE_TYPE = "param"
        private const val PARAM_PRODUCT_PAGE = "productPage"
        private const val PARAM_LAYOUTS = "layouts"
        private const val PRODUCT_CARD_VERSION = "productCardVersion"
        private const val PRODUCT_CARD_VERSION_V5 = "v5"
        private const val REFRESH_TYPE = "refreshType"
        private const val BYTEDANCE_SESSION_ID = "bytedanceSessionID"

        private const val LAYOUTS_VALUE = "product,recom_card,banner_ads,video"
    }
}

