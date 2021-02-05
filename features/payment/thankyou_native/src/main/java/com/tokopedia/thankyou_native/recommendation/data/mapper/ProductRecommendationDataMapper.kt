package com.tokopedia.thankyou_native.recommendation.data.mapper

import android.content.Context
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.data.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.data.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.di.qualifier.IODispatcher
import com.tokopedia.thankyou_native.recommendation.di.qualifier.RecommendationApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductRecommendationDataMapper @Inject constructor(
        @RecommendationApplicationContext private val context: Context,
        @IODispatcher private val ioDispatcher: dagger.Lazy<CoroutineDispatcher>) {

    suspend fun getProductRecommendationData(recommendationWidget: RecommendationWidget)
            : ProductRecommendationData? {
        val recommendationItemList = recommendationWidget.recommendationItemList
        if (!recommendationItemList.isNullOrEmpty()) {
            val thankYouProductCardModelList = getProductCardModel(recommendationItemList)
            return ProductRecommendationData(
                    recommendationWidget.title,
                    getMaxHeight(thankYouProductCardModelList),
                    thankYouProductCardModelList)
        }
        return null
    }

    private fun getProductCardModel(recommendationItemList: List<RecommendationItem>)
            : List<ThankYouProductCardModel> {
        return recommendationItemList.map { recommendationItem ->
            ThankYouProductCardModel(recommendationItem,
                    ProductCardModel(
                            slashedPrice = recommendationItem.slashedPrice,
                            productName = recommendationItem.name,
                            formattedPrice = recommendationItem.price,
                            productImageUrl = recommendationItem.imageUrl,
                            isTopAds = recommendationItem.isTopAds,
                            discountPercentage = if (recommendationItem.discountPercentageInt > 0)
                                recommendationItem.discountPercentage else "",
                            reviewCount = recommendationItem.countReview,
                            ratingCount = recommendationItem.rating,
                            shopLocation = recommendationItem.location,
                            shopBadgeList = recommendationItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = recommendationItem.isFreeOngkirActive,
                                    imageUrl = recommendationItem.freeOngkirImageUrl
                            ),
                            labelGroupList = recommendationItem.labelGroupList.map { recommendationLabel ->
                                ProductCardModel.LabelGroup(
                                        position = recommendationLabel.position,
                                        title = recommendationLabel.title,
                                        type = recommendationLabel.type
                                )
                            },
                            hasThreeDots = true
                    )
            )
        }
    }

    private suspend fun getMaxHeight(thankYouProductCardModelList: List<ThankYouProductCardModel>)
            : Int {
        val data = thankYouProductCardModelList.map {
            it.productCardModel
        }
        return data.getMaxHeightForGridView(
                context = context,
                coroutineDispatcher = ioDispatcher.get(),
                productImageWidth = context.resources.getDimensionPixelSize(R.dimen.thank_success_img_height)
        )
    }

}