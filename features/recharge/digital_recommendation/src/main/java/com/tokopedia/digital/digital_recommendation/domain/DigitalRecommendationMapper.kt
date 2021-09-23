package com.tokopedia.digital.digital_recommendation.domain

import com.tokopedia.digital.digital_recommendation.data.DigitalRecommendationResponse
import com.tokopedia.digital.digital_recommendation.data.TrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationTrackingModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationType

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationMapper {
    companion object {
        fun transform(response: DigitalRecommendationResponse): List<DigitalRecommendationModel> {
            val newData = arrayListOf<DigitalRecommendationModel>()

            for (item in response.personalizedItems.recommendationItems) {
                newData.add(
                        DigitalRecommendationModel(
                                iconUrl = item.mediaURL,
                                categoryName = if (item.trackingData.itemLabel.isNotEmpty()) {
                                    when (item.trackingData.itemLabel) {
                                        TYPE_PRODUCT_RECOMMENDATION -> item.title
                                        TYPE_CATEGORY -> item.trackingData.categoryName
                                        else -> item.title
                                    }
                                } else "",
                                productName = item.subtitle,
                                clientNumber = item.label1,
                                applink = item.appLink,
                                tracking = transform(item.trackingData),
                                type = if (item.trackingData.itemLabel.isNotEmpty()) {
                                    when (item.trackingData.itemLabel) {
                                        TYPE_PRODUCT_RECOMMENDATION -> DigitalRecommendationType.PRODUCT
                                        else -> DigitalRecommendationType.CATEGORY
                                    }
                                } else DigitalRecommendationType.CATEGORY
                        )
                )
            }

            return newData
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