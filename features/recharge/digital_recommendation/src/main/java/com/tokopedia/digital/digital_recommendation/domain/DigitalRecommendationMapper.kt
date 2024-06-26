package com.tokopedia.digital.digital_recommendation.domain

import com.tokopedia.digital.digital_recommendation.data.DigitalRecommendationResponse
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemUnifyModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationTrackingModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardActionModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardCampaignModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardInfoModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardPriceModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardRatingModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardSoldPercentageModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyConst
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel
import com.tokopedia.unifycomponents.Label

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationMapper {
    companion object {

        fun transform(response: DigitalRecommendationResponse): DigitalRecommendationModel {
            val items = response.personalizedItems.recommendationItems.map {
                val discountLabel = when {
                    it.cashback.isNotEmpty() -> it.cashback
                    it.specialDiscount.isNotEmpty() -> it.specialDiscount
                    else -> it.discount
                }

                val discountType = when {
                    it.cashback.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_CASHBACK
                    it.specialDiscount.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_SPECIAL
                    it.discount.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_SLASH
                    else -> ""
                }

                val discountLabelType = when (discountType) {
                    DigitalUnifyConst.DISCOUNT_CASHBACK -> Label.HIGHLIGHT_LIGHT_GREEN
                    DigitalUnifyConst.DISCOUNT_SPECIAL, DigitalUnifyConst.DISCOUNT_SLASH -> Label.HIGHLIGHT_LIGHT_RED
                    else -> 0
                }

                val itemUnify = DigitalUnifyModel(
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
                        discountLabelType = discountLabelType,
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

                val itemTracking = DigitalRecommendationTrackingModel(
                    typeName = it.trackingData.__typename,
                    pricePlain = it.trackingData.pricePlain,
                    businessUnit = it.trackingData.businessUnit,
                    categoryId = it.trackingData.categoryID,
                    categoryName = it.trackingData.categoryName,
                    itemLabel = it.trackingData.itemLabel,
                    itemType = it.trackingData.itemType,
                    operatorId = it.trackingData.operatorID,
                    productId = it.trackingData.productID
                )

                return@map DigitalRecommendationItemUnifyModel(
                    unify = itemUnify,
                    tracking = itemTracking
                )
            }

            return DigitalRecommendationModel(
                userType = response.personalizedItems.trackingData.userType,
                title = response.personalizedItems.title,
                items = items
            )
        }
    }
}
