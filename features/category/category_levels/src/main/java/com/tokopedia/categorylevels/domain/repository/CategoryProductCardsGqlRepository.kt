package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.LihatSemua
import com.tokopedia.discovery2.data.productcarditem.FreeOngkir
import com.tokopedia.discovery2.data.productcarditem.LabelsGroup
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getPageInfo
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
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
    }

    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val page = queryParamterMap[RPC_PAGE_NUMBER] as String
        return if(productComponentName  == ComponentNames.CategoryBestSeller.componentName){
            val recommendationData =
                    recommendationUseCase.getData(getRecommendationRequestParam(page, getPageInfo(pageEndPoint).id.toString(), true))
            mapRecommendationToDiscoveryResponse(componentId, recommendationData, productComponentName)
        } else {
            val recommendationData =
                    recommendationUseCase.getData(createRequestParams(page, getPageInfo(pageEndPoint).id.toString(), getComponent(componentId, pageEndPoint)))
            mapRecommendationToDiscoveryResponse(componentId, recommendationData, productComponentName)
        }
    }

    private fun getRecommendationRequestParam(page: String, componentId: String, isBestSeller : Boolean, queryParam : String = ""): GetRecommendationRequestParam {
        return GetRecommendationRequestParam(
                pageNumber = page.toIntOrZero(),
                pageName = if(isBestSeller) "category_best_seller" else "category_page",
                queryParam = queryParam,
                categoryIds = arrayListOf(componentId),
                xDevice = "android",
                xSource = "category_landing_page"
        )
    }

    private fun createRequestParams(page: String, componentId: String, component: ComponentsItem?): GetRecommendationRequestParam {
        var queryParam =""
        component?.selectedFilters?.forEach {
            queryParam = queryParam.plus("&${it.key}=${it.value}")
        }
        component?.selectedSort?.forEach {
            queryParam = queryParam.plus("&${it.key}=${it.value}")
        }
        return getRecommendationRequestParam(page, componentId, false, queryParam)
    }

    private fun mapRecommendationToDiscoveryResponse(componentId: String, recommendationData: List<RecommendationWidget>, productComponentName: String?): ArrayList<ComponentsItem> {
        val components = arrayListOf<ComponentsItem>()
        recommendationData.firstOrNull()?.recommendationItemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            val dataItem = DataItem()
            componentsItem.position = index
            componentsItem.parentComponentId = componentId
            if(productComponentName == ComponentNames.CategoryBestSeller.componentName) {
                componentsItem.name = ComponentNames.ProductCardCarouselItem.componentName
                componentsItem.lihatSemua = LihatSemua(applink = recommendationData.firstOrNull()?.seeMoreAppLink ?: "", header = recommendationData.firstOrNull()?.title ?: "")
                dataItem.typeProductCard = ComponentNames.ProductCardCarouselItem.componentName
            }
            else {
                dataItem.typeProductCard = ComponentNames.ProductCardRevampItem.componentName
                componentsItem.name = ComponentNames.ProductCardRevampItem.componentName
            }
            val dataItems = mutableListOf<DataItem>()
            val labelsGroupList = arrayListOf<LabelsGroup>()
            it.labelGroupList.forEach {
                labelsGroupList.add(LabelsGroup(it.position, it.title, it.type, it.imageUrl))
            }
            dataItem.id = it.productId.toString()
            dataItem.productId = it.productId.toString()
            dataItem.name = it.name
            dataItem.price = it.price
            dataItem.rating = it.rating.toString()
            dataItem.averageRating = it.ratingAverage
            dataItem.imageUrlMobile = it.imageUrl
            dataItem.isTopads = it.isTopAds
            dataItem.topadsClickUrl = it.clickUrl
            dataItem.topadsViewUrl = it.trackerImageUrl
            dataItem.shopId = it.shopId.toString()
            dataItem.shopName = it.shopName
            dataItem.shopLocation = it.location
            dataItem.discountedPrice = it.slashedPrice
            dataItem.discountPercentage = it.discountPercentageInt.toString()
            dataItem.departmentID = it.departmentId
            dataItem.hasThreeDots = true
            dataItem.isWishList = it.isWishlist
            dataItem.wishlistUrl = it.wishlistUrl
            dataItem.countReview = it.countReview.toString()
            dataItem.freeOngkir = FreeOngkir(it.freeOngkirImageUrl, it.isFreeOngkirActive)
            dataItem.applinks = it.appUrl
            dataItem.goldMerchant = it.isGold
            dataItem.officialStore = it.isOfficial
            dataItem.labelsGroupList = labelsGroupList
            dataItems.add(dataItem)
            componentsItem.id = it.productId.toString()
            componentsItem.data = dataItems
            components.add(componentsItem)
        }
        return components
    }
}