package com.tokopedia.digital.digital_recommendation.utils

import android.os.Bundle
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.track.TrackApp

/**
 * @author by furqan on 23/09/2021
 */
class DigitalRecommendationAnalytics {

    fun impressionDigitalRecommendationItems(digitalRecommendationModel: DigitalRecommendationModel,
                                             additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
                                             index: Int,
                                             userId: String) {
        val bundle = Bundle().apply {
            putString(DigitalRecommendationKeys.EVENT, DigitalRecommendationEvents.VIEW_ITEM_LIST)
            putString(DigitalRecommendationKeys.EVENT_ACTION, DigitalRecommendationActions.IMPRESSION)
            putString(DigitalRecommendationKeys.EVENT_CATEGORY, DigitalRecommendationValues.CATEGORY_PURCHASE_LIST_MP)
            putString(
                    DigitalRecommendationKeys.EVENT_LABEL,
                    String.format("%s - %s - %d - %d - %s - %s - %s - %s",
                            digitalRecommendationModel.tracking.itemType,
                            additionalTrackingData.userType,
                            additionalTrackingData.widgetPosition,
                            index + 1,
                            digitalRecommendationModel.tracking.categoryId,
                            digitalRecommendationModel.tracking.operatorId,
                            digitalRecommendationModel.tracking.productId,
                            additionalTrackingData.pgCategory
                    )
            )
            putString(DigitalRecommendationKeys.BUSINESS_UNIT, digitalRecommendationModel.tracking.businessUnit)
            putString(DigitalRecommendationKeys.CURRENT_SITE, DigitalRecommendationValues.CURRENT_SITE)
            putParcelableArrayList(DigitalRecommendationKeys.ITEMS, arrayListOf(
                    Bundle().also {
                        it.putInt(DigitalRecommendationKeys.INDEX, index)
                        it.putString(DigitalRecommendationKeys.ITEM_BRAND, digitalRecommendationModel.tracking.productId)
                        it.putString(DigitalRecommendationKeys.ITEM_CATEGORY, digitalRecommendationModel.tracking.categoryId)
                        it.putString(DigitalRecommendationKeys.ITEM_ID, digitalRecommendationModel.tracking.productId)
                        it.putString(DigitalRecommendationKeys.ITEM_NAME, digitalRecommendationModel.productName)
                        it.putString(DigitalRecommendationKeys.ITEM_VARIANT, digitalRecommendationModel.tracking.itemType)
                        it.putString(DigitalRecommendationKeys.PRICE, digitalRecommendationModel.price)
                    }
            ))
            putString(DigitalRecommendationKeys.USER_ID, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalRecommendationEvents.VIEW_ITEM_LIST, bundle)
    }

    fun clickDigitalRecommendationItems(digitalRecommendationModel: DigitalRecommendationModel,
                                        additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
                                        index: Int,
                                        userId: String) {
        val bundle = Bundle().apply {
            putString(DigitalRecommendationKeys.EVENT, DigitalRecommendationEvents.SELECT_CONTENT)
            putString(DigitalRecommendationKeys.EVENT_ACTION, DigitalRecommendationActions.CLICK)
            putString(DigitalRecommendationKeys.EVENT_CATEGORY, DigitalRecommendationValues.CATEGORY_PURCHASE_LIST_MP)
            putString(
                    DigitalRecommendationKeys.EVENT_LABEL,
                    String.format("%s - %s - %d - %d - %s - %s - %s - %s",
                            digitalRecommendationModel.tracking.itemType,
                            additionalTrackingData.userType,
                            additionalTrackingData.widgetPosition,
                            index + 1,
                            digitalRecommendationModel.tracking.categoryId,
                            digitalRecommendationModel.tracking.operatorId,
                            digitalRecommendationModel.tracking.productId,
                            additionalTrackingData.pgCategory
                    )
            )
            putString(DigitalRecommendationKeys.BUSINESS_UNIT, digitalRecommendationModel.tracking.businessUnit)
            putString(DigitalRecommendationKeys.CURRENT_SITE, DigitalRecommendationValues.CURRENT_SITE)
            putParcelableArrayList(DigitalRecommendationKeys.ITEMS, arrayListOf(
                    Bundle().also {
                        it.putInt(DigitalRecommendationKeys.INDEX, index)
                        it.putString(DigitalRecommendationKeys.ITEM_BRAND, digitalRecommendationModel.tracking.productId)
                        it.putString(DigitalRecommendationKeys.ITEM_CATEGORY, digitalRecommendationModel.tracking.categoryId)
                        it.putString(DigitalRecommendationKeys.ITEM_ID, digitalRecommendationModel.tracking.productId)
                        it.putString(DigitalRecommendationKeys.ITEM_NAME, digitalRecommendationModel.productName)
                        it.putString(DigitalRecommendationKeys.ITEM_VARIANT, digitalRecommendationModel.tracking.itemType)
                        it.putString(DigitalRecommendationKeys.PRICE, digitalRecommendationModel.price)
                    }
            ))
            putString(DigitalRecommendationKeys.USER_ID, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalRecommendationEvents.SELECT_CONTENT, bundle)
    }

}

class DigitalRecommendationKeys {
    companion object {
        const val EVENT = "event"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_LABEL = "eventLabel"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val ITEMS = "items"
        const val INDEX = "index"
        const val ITEM_BRAND = "item_brand"
        const val ITEM_CATEGORY = "item_category"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_VARIANT = "item_variant"
        const val PRICE = "price"
        const val USER_ID = "userId"
    }
}

class DigitalRecommendationEvents {
    companion object {
        const val VIEW_ITEM_LIST = "view_item_list"
        const val SELECT_CONTENT = "select_content"
    }
}

class DigitalRecommendationActions {
    companion object {
        const val IMPRESSION = "impression on widget recommendation"
        const val CLICK = "click on widget recommendation"
    }
}

class DigitalRecommendationValues {
    companion object {
        const val CATEGORY_PURCHASE_LIST_MP = "my purchase list detail - mp"

        const val CURRENT_SITE = "tokopediadigital"
    }
}