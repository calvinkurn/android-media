package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.categorylevels.data.raw.GQL_WIDGET_QUERY
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

class CategoryProductCardsGqlRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {
    companion object {
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private val PAGE_NUMBER = "pageNumber"
        private val PAGE_NAME = "pageName"
        private val QUERY_PARAM = "queryParam"
        private val PRODUCT_IDS = "productIDs"
        private val X_SOURCE = "xSource"
        private val X_DEVICE = "xDevice"
    }

    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val page = queryParamterMap[RPC_PAGE_NUMBER] as String
        val recommendationData = getGQLData(GQL_WIDGET_QUERY,
                RecommendationEntity::class.java,
                createRequestParams(page, componentId)
        )
        return mapRecommendationToDiscoveryResponse(recommendationData.productRecommendationWidget?.data ?: listOf())
    }

    private fun createRequestParams(page : String, componentId:String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PAGE_NUMBER] = page.toIntOrZero()
        request[PAGE_NAME] = "category_page"
        request[QUERY_PARAM] = ""
        request[PRODUCT_IDS] = componentId
        request[X_DEVICE] = "android"
        request[X_SOURCE] = "category_landing_page"
        return request
    }

    private fun mapRecommendationToDiscoveryResponse(recommendationData: List<RecommendationEntity.RecomendationData>): ArrayList<ComponentsItem> {
        val components = arrayListOf<ComponentsItem>()
        recommendationData[0].recommendation?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = ComponentNames.ProductCardRevampItem.componentName
            val dataItems = mutableListOf<DataItem>()
            val dataItem = DataItem()
            dataItem.id = it.id.toString()
            dataItem.name = it.name
            dataItem.price = it.price
            dataItem.rating = it.rating.toString()
            dataItem.imageUrlMobile = it.imageUrl
            dataItem.isTopads = it.isIsTopads
            dataItem.shopId = it.shop?.id.toString()
            dataItem.shopName = it.shop?.name
            dataItem.shopLocation = it.shop?.city
            dataItem.applinks = it.appUrl
            dataItem.typeProductCard = ComponentNames.ProductCardRevampItem.componentName
            dataItems.add(dataItem)
            componentsItem.data = dataItems
            components.add(componentsItem)
        }
        return components
    }
}