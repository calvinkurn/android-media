package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStateUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.FeaturedProductsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel
import javax.inject.Inject

class ProductRecommendationMapper @Inject constructor() {

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

    fun getEmptyProductListDefaultUIModel(): List<EmptyStateUiModel>{
        return listOf(EmptyStateUiModel())
    }

    fun convertProductItemToFeaturedProductsUiModel(products: List<ProductListUiModel>) : List<FeaturedProductsUiModel>{
        val featuredProductsList = mutableListOf<FeaturedProductsUiModel>()
        products.forEach {
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

    fun convertToGroupItemUiModel(list: List<GroupListDataItem>): List<GroupItemUiModel>{
        val groupList = mutableListOf<GroupItemUiModel>()
        list.forEach {
            groupList.add(
                GroupItemUiModel(
                    groupId = it.groupId,
                    groupName = it.groupName,
                    isSelected = false,
                    keywordCount = 2,
                    productCount = 3,
                )
            )
        }
        return groupList
    }
}
