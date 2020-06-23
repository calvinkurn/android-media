package com.tokopedia.review.feature.reviewlist.util.mapper

import com.tokopedia.review.common.util.ReviewSellerConstant.LAST_MONTH_KEY
import com.tokopedia.review.common.util.ReviewSellerConstant.LAST_WEEK_KEY
import com.tokopedia.review.common.util.ReviewSellerConstant.LAST_YEAR_KEY
import com.tokopedia.review.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import java.text.SimpleDateFormat
import java.util.*

object SellerReviewProductListMapper {

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
            period = getPastDateCalculate(productGetProductRatingOverallByShop.filterBy ?: LAST_WEEK_KEY)
        }
    }

    fun getPastDateCalculate(filterDateString: String): String {
        val cal = Calendar.getInstance(Locale("in","id"))
        val pastDateInterval = when (filterDateString) {
            LAST_WEEK_KEY -> 7
            LAST_MONTH_KEY -> 30
            LAST_YEAR_KEY -> 365
            else -> 7
        }
        cal.add(Calendar.DATE, - pastDateInterval)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        dateFormat.timeZone = cal.timeZone
        return dateFormat.format(cal.time)
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