package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

class CategoryProductCardsGqlRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {

    @Inject
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val recommendationData = getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                        pageName = "category_page",
                        pageNumber = 1,
                        productIds = arrayListOf(componentId),
                        queryParam = ""
                )
        )
        return mapRecommendationToDiscoveryResponse(recommendationData)
    }

    private fun mapRecommendationToDiscoveryResponse(recommendationData: List<RecommendationWidget>): ArrayList<ComponentsItem> {
        val components = arrayListOf<ComponentsItem>()
        for(recom in recommendationData){
            components.add(ComponentsItem())
        }
        return components
    }
}