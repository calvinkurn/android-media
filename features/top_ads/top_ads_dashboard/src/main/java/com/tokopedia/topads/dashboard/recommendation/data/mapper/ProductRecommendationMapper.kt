package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import javax.inject.Inject

class ProductRecommendationMapper @Inject constructor() {

    var groupList = listOf<GroupListDataItem>()
    var totalAdsKeywords = listOf<CountDataItem>()

    fun convertToProductItemUiModel(products: List<ProductRecommendation>): List<ProductItemUiModel> {
        val productList = mutableListOf<ProductItemUiModel>()
        products.forEach {
            productList.add(
                ProductItemUiModel(
                    productId = it.productId,
                    productName = it.productName,
                    imgUrl = it.imgUrl,
                    searchCount = it.searchCount
                )
            )
        }
        return productList
    }

    fun getEmptyProductListDefaultUiModel(): List<EmptyProductListUiModel>{
        return listOf(EmptyProductListUiModel())
    }

    fun convertProductItemToFeaturedProductsUiModel(products: List<ProductListUiModel>?) : List<FeaturedProductsUiModel>{
        val featuredProductsList = mutableListOf<FeaturedProductsUiModel>()
        products?.forEach {
            (it as? ProductItemUiModel)?.apply {
                featuredProductsList.add(
                    FeaturedProductsUiModel(
                        productId = this.productId,
                        imgUrl = this.imgUrl
                    )
                )
            }
        }
        return featuredProductsList
    }

    fun convertToGroupItemUiModel(
        groupList: List<GroupListDataItem>,
        totalAdsKeywords: List<CountDataItem>
    ): List<GroupItemUiModel>{
        val list = mutableListOf<GroupItemUiModel>()
        groupList.forEach {
            val item = getTotalAdsForGroupId(it.groupId, totalAdsKeywords)
            list.add(
                GroupItemUiModel(
                    groupName = it.groupName,
                    groupId = it.groupId,
                    isSelected = false,
                    keywordCount = item?.totalKeywords ?: 0,
                    productCount = item?.totalProducts ?: 0
                )
            )
        }
        return list
    }

    private fun getTotalAdsForGroupId(groupId: String, totalAdsKeywords: List<CountDataItem>): CountDataItem? {
        return totalAdsKeywords.filter { it.iD == groupId }.firstOrNull()
    }

    fun getListOfGroupIds(groupList: DashGroupListResponse): List<String>{
        val groupIds = mutableListOf<String>()
        groupList.getTopadsDashboardGroups.data.forEach {
            groupIds.add(it.groupId)
        }
        return groupIds
    }
}
