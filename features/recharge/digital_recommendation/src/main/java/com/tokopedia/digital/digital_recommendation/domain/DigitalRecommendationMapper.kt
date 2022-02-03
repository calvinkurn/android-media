package com.tokopedia.digital.digital_recommendation.domain

import com.tokopedia.digital.digital_recommendation.data.DigitalRecommendationResponse
import com.tokopedia.digital.digital_recommendation.data.TrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationTrackingModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationType
import com.tokopedia.recharge_component.digital_card.presentation.model.*
import com.tokopedia.unifycomponents.Label

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationMapper {
    companion object {
        fun transform(response: DigitalRecommendationResponse): DigitalRecommendationModel {
            val newData = arrayListOf<DigitalRecommendationItemModel>()

            for (item in response.personalizedItems.recommendationItems) {
                newData.add(
                        DigitalRecommendationItemModel(
                                iconUrl = item.mediaURL,
                                categoryName = if (item.trackingData.itemLabel.isNotEmpty()) {
                                    when (item.trackingData.itemLabel) {
                                        TYPE_PRODUCT_RECOMMENDATION -> item.title
                                        TYPE_CATEGORY -> item.trackingData.categoryName
                                        else -> item.title
                                    }
                                } else item.title,
                                productName = item.subtitle,
                                applink = item.appLink,
                                tracking = transform(item.trackingData),
                                type = if (item.trackingData.itemLabel.isNotEmpty()) {
                                    when (item.trackingData.itemLabel) {
                                        TYPE_PRODUCT_RECOMMENDATION -> DigitalRecommendationType.PRODUCT
                                        else -> DigitalRecommendationType.CATEGORY
                                    }
                                } else DigitalRecommendationType.CATEGORY,
                                discountTag = item.label1,
                                beforePrice = item.label2,
                                price = item.label3
                        )
                )
            }

            return DigitalRecommendationModel(
                    userType = response.personalizedItems.trackingData.userType,
                    title = response.personalizedItems.title,
                    items = newData
            )
        }

        fun mapResponseToDigitalUnify(response: DigitalRecommendationResponse): List<DigitalUnifyModel>{

            return response.personalizedItems.recommendationItems.map {

                val discountLabel = when{
                    it.cashback.isNotEmpty() -> it.cashback
                    it.specialDiscount.isNotEmpty() -> it.specialDiscount
                    else -> it.label3
                }

                val discountType = when{
                    it.cashback.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_CASHBACK
                    it.specialDiscount.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_SPECIAL
                    it.label3.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_SLASH
                    else -> ""
                }

                val discountLabelType = when(discountType){
                    DigitalUnifyConst.DISCOUNT_CASHBACK -> Label.HIGHLIGHT_LIGHT_GREEN
                    DigitalUnifyConst.DISCOUNT_SPECIAL, DigitalUnifyConst.DISCOUNT_SLASH -> Label.HIGHLIGHT_LIGHT_RED
                    else -> 0
                }

                return@map DigitalUnifyModel(
                    id = it.id,
                    mediaUrl = it.mediaURL,
                    mediaType = response.personalizedItems.mediaUrlType,
                    mediaTitle = it.mediaUrlTitle,
                    iconUrl = it.iconURL,
                    iconBackgroundColor = it.backgroundColor,
                    campaign = DigitalCardCampaignModel(
                        text = it.campaignLabelText,
                        textColor = it.campaignLabelTextColor,
                        backgroundUrl = it.campaignLabelBackgroundUrl
                    ),
                    productInfoLeft = DigitalCardInfoModel(
                        text = it.productInfo1.text,
                        textColor = it.productInfo1.color
                    ),
                    productInfoRight = DigitalCardInfoModel(
                        text = it.productInfo2.text,
                        textColor = it.productInfo2.color
                    ),
                    title = it.title,
                    rating = DigitalCardRatingModel(
                        ratingType = it.ratingType,
                        rating = it.rating,
                        review = it.review
                    ),
                    specialInfo = DigitalCardInfoModel(
                        text = it.specialInfoText,
                        textColor = it.specialInfoColor
                    ),
                    priceData = DigitalCardPriceModel(
                        price = it.price,
                        discountLabelType =discountLabelType,
                        discountLabel = discountLabel,
                        discountType = discountType,
                        slashedPrice = it.slashedPrice,
                        pricePrefix = it.pricePrefix,
                        priceSuffix = it.priceSuffix
                    ),
                    cashback = it.cashback,
                    subtitle = it.subtitle,
                    soldPercentage = DigitalCardSoldPercentageModel(
                        showPercentage = it.showSoldPercentage,
                        value = it.soldPercentageValue,
                        label = it.soldPercentageLabel,
                        labelColor = it.soldPercentageLabelColor
                    ),
                    actionButton = DigitalCardActionModel(
                        text = it.textLink,
                        applink = it.appLink,
                        buttonType = ""
                    )
                )
            }
        }

        private fun transform(trackingData: TrackingData): DigitalRecommendationTrackingModel =
                DigitalRecommendationTrackingModel(
                        typeName = trackingData.__typename,
                        businessUnit = trackingData.businessUnit,
                        categoryId = trackingData.categoryID,
                        categoryName = trackingData.categoryName,
                        itemLabel = trackingData.itemLabel,
                        itemType = trackingData.itemType,
                        operatorId = trackingData.operatorID,
                        productId = trackingData.productID
                )

        private const val TYPE_PRODUCT_RECOMMENDATION = "product"
        private const val TYPE_CATEGORY = "category"
    }
}