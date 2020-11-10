package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

class CategoryProductCardsGqlRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {

    @Inject
    lateinit var recommendationUseCase: GetRecommendationUseCase

    companion object {
        //TODO niranjan move query params to repo
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_CATEGORY_ID = "rpc_page_number"
    }

    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val page = queryParamterMap[RPC_PAGE_NUMBER] as String
        val recommendationData =
                recommendationUseCase.getData(createRequestParams(page, componentId))
        return mapRecommendationToDiscoveryResponse(recommendationData)
    }

    private fun createRequestParams(page: String, componentId: String): GetRecommendationRequestParam {
        return GetRecommendationRequestParam(
                pageNumber = page.toIntOrZero(),
                pageName = "category_page",
                queryParam = "",
                categoryIds = arrayListOf(componentId),
                xDevice = "android",
                xSource = "category_landing_page"
        )
    }

    private fun mapRecommendationToDiscoveryResponse(recommendationData: List<RecommendationWidget>): ArrayList<ComponentsItem> {
        val components = arrayListOf<ComponentsItem>()
        recommendationData[0].recommendationItemList.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = ComponentNames.ProductCardRevampItem.componentName
            val dataItems = mutableListOf<DataItem>()
            val dataItem = DataItem()
            dataItem.id = it.productId.toString()
            dataItem.name = it.name
            dataItem.price = it.price
            dataItem.rating = it.rating.toString()
            dataItem.imageUrlMobile = it.imageUrl
            dataItem.isTopads = it.isTopAds
            dataItem.shopId = it.shopId.toString()
            dataItem.shopName = it.shopName
            dataItem.shopLocation = it.location
            dataItem.discountPercentage = it.discountPercentage
            dataItem.applinks = it.appUrl
            dataItem.typeProductCard = ComponentNames.ProductCardRevampItem.componentName
            dataItems.add(dataItem)
            componentsItem.data = dataItems
            components.add(componentsItem)
        }
        return components
    }
}