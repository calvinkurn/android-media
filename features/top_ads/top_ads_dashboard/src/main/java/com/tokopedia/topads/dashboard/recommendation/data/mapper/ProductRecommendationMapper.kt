package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyProductListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyGroupListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.FailedGroupListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.FeaturedProductsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.LoadingGroupsUiModel
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
                    searchCount = it.searchCount,
                    isSelected = true
                )
            )
        }
        return productList
    }

    fun getEmptyProductListDefaultUiModel(): List<EmptyProductListUiModel> {
        return listOf(EmptyProductListUiModel())
    }

    fun getEmptyGroupListDefaultUiModel(): List<EmptyGroupListUiModel> {
        return listOf(EmptyGroupListUiModel())
    }

    fun getFailedGroupStateDefaultUiModel(): List<FailedGroupListUiModel> {
        return listOf(FailedGroupListUiModel())
    }

    fun getGroupShimmerUiModel(): List<LoadingGroupsUiModel> {
        val list = mutableListOf<LoadingGroupsUiModel>()
        // adding empty data for shimmer loader into UiModel
        for (n in 1..6)
            list.add(LoadingGroupsUiModel(n.toString()))
        return list
    }

    fun convertProductItemToFeaturedProductsUiModel(products: List<ProductListUiModel>?): List<FeaturedProductsUiModel> {
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
    ): List<GroupItemUiModel> {
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

    private fun getTotalAdsForGroupId(
        groupId: String,
        totalAdsKeywords: List<CountDataItem>
    ): CountDataItem? {
        return totalAdsKeywords.filter { it.iD == groupId }.firstOrNull()
    }

    fun getListOfGroupIds(groupList: DashGroupListResponse): List<String> {
        val groupIds = mutableListOf<String>()
        groupList.getTopadsDashboardGroups.data.forEach {
            groupIds.add(it.groupId)
        }
        return groupIds
    }
}
