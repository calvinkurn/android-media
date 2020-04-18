package com.tokopedia.reviewseller.feature.reviewlist.util.mapper

import com.tokopedia.reviewseller.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import java.util.*

object ReviewSellerMapper {

    fun mapToProductReviewListUiModel(productShopRatingAggregate: ProductReviewListResponse.ProductShopRatingAggregate): List<ProductReviewUiModel> {
        val productReviewListUiModel = mutableListOf<ProductReviewUiModel>()

        productShopRatingAggregate.data.forEach {
            productReviewListUiModel.add(ProductReviewUiModel(
                    productID = it.product.productID,
                    productImageUrl = it.product.productImageURL,
                    productName = it.product.productName,
                    rating = it.rating,
                    reviewCount = it.reviewCount
            ))
        }
        return productReviewListUiModel
    }

    fun mapToProductRatingOverallModel(productGetProductRatingOverallByShop:
                                       ProductRatingOverallResponse.ProductGetProductRatingOverallByShop): ProductRatingOverallUiModel {
        return ProductRatingOverallUiModel().apply {
            rating = productGetProductRatingOverallByShop.rating
            reviewCount = productGetProductRatingOverallByShop.reviewCount
            period = productGetProductRatingOverallByShop.filterBy
        }
    }

    fun mapToItemUnifyList(list: Array<String>): ArrayList<ListItemUnify> {
        val itemUnifyList: ArrayList<ListItemUnify> = arrayListOf()
        list.map {
            val data = ListItemUnify(title = it, description = "")
            data.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            itemUnifyList.add(data)
        }
        return itemUnifyList
    }
}