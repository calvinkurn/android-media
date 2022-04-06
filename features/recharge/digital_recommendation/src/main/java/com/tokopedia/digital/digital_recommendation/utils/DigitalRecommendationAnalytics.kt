package com.tokopedia.digital.digital_recommendation.utils

import android.os.Bundle
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemUnifyModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.track.TrackApp

/**
 * @author by furqan on 23/09/2021
 */
class DigitalRecommendationAnalytics {

    fun impressionDigitalRecommendationItems(digitalRecommendationModel: DigitalRecommendationItemUnifyModel,
                                             additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
                                             index: Int,
                                             userId: String,
                                             page: DigitalRecommendationPage?) {
        val bundle = Bundle().apply {
            putString(DigitalRecommendationKeys.EVENT, DigitalRecommendationEvents.VIEW_ITEM_LIST)
            putString(DigitalRecommendationKeys.EVENT_ACTION, DigitalRecommendationActions.IMPRESSION)
            putString(DigitalRecommendationKeys.EVENT_CATEGORY,
                    when (page) {
                        DigitalRecommendationPage.PHYSICAL_GOODS -> DigitalRecommendationValues.CATEGORY_PURCHASE_LIST_MP
                        DigitalRecommendationPage.DIGITAL_GOODS -> DigitalRecommendationValues.CATEGORY_PURCHASE_LIST_DG
                        else -> ""
                    })
            putString(
                    DigitalRecommendationKeys.EVENT_LABEL,
                    String.format("%s - %s - %s - %d - %s - %s - %s - %s",
                            digitalRecommendationModel.tracking.itemType,
                            additionalTrackingData.userType,
                            additionalTrackingData.widgetPosition,
                            index + 1,
                            digitalRecommendationModel.tracking.categoryId,
                            digitalRecommendationModel.tracking.operatorId,
                            digitalRecommendationModel.tracking.productId,
                            when (page) {
                                DigitalRecommendationPage.PHYSICAL_GOODS -> additionalTrackingData.pgCategories.joinToString(separator = ",")
                                DigitalRecommendationPage.DIGITAL_GOODS -> additionalTrackingData.dgCategories.joinToString(separator = ",")
                                else -> ""
                            }
                    )
            )
            putString(DigitalRecommendationKeys.BUSINESS_UNIT, digitalRecommendationModel.tracking.businessUnit)
            putString(DigitalRecommendationKeys.CURRENT_SITE, DigitalRecommendationValues.CURRENT_SITE)
            putParcelableArrayList(DigitalRecommendationKeys.ITEMS, arrayListOf(
                    Bundle().also {
                        it.putInt(DigitalRecommendationKeys.INDEX, index + 1)
                        it.putString(DigitalRecommendationKeys.ITEM_BRAND, if (digitalRecommendationModel.tracking.productId.isNotEmpty())
                            digitalRecommendationModel.tracking.productId else "0")
                        it.putString(DigitalRecommendationKeys.ITEM_CATEGORY, "${digitalRecommendationModel.tracking.categoryId} - ${digitalRecommendationModel.tracking.categoryName}")
                        it.putString(DigitalRecommendationKeys.ITEM_ID, if (digitalRecommendationModel.tracking.productId.isNotEmpty())
                            digitalRecommendationModel.tracking.productId else "0")
                        it.putString(DigitalRecommendationKeys.ITEM_NAME, digitalRecommendationModel.unify.title)
                        it.putString(DigitalRecommendationKeys.ITEM_VARIANT, digitalRecommendationModel.tracking.itemType)
                        it.putString(DigitalRecommendationKeys.PRICE, digitalRecommendationModel.tracking.pricePlain)
                    }
            ))
            putString(DigitalRecommendationKeys.USER_ID, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalRecommendationEvents.VIEW_ITEM_LIST, bundle)
    }

    fun clickDigitalRecommendationItems(digitalRecommendationModel: DigitalRecommendationItemUnifyModel,
                                        additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
                                        index: Int,
                                        userId: String,
                                        page: DigitalRecommendationPage?) {
        val bundle = Bundle().apply {
            putString(DigitalRecommendationKeys.EVENT, DigitalRecommendationEvents.SELECT_CONTENT)
            putString(DigitalRecommendationKeys.EVENT_ACTION, DigitalRecommendationActions.CLICK)
            putString(DigitalRecommendationKeys.EVENT_CATEGORY,
                    when (page) {
                        DigitalRecommendationPage.PHYSICAL_GOODS -> DigitalRecommendationValues.CATEGORY_PURCHASE_LIST_MP
                        DigitalRecommendationPage.DIGITAL_GOODS -> DigitalRecommendationValues.CATEGORY_PURCHASE_LIST_DG
                        else -> ""
                    })
            putString(
                    DigitalRecommendationKeys.EVENT_LABEL,
                    String.format("%s - %s - %s - %d - %s - %s - %s - %s",
                            digitalRecommendationModel.tracking.itemType,
                            additionalTrackingData.userType,
                            additionalTrackingData.widgetPosition,
                            index + 1,
                            digitalRecommendationModel.tracking.categoryId,
                            digitalRecommendationModel.tracking.operatorId,
                            digitalRecommendationModel.tracking.productId,
                            when (page) {
                                DigitalRecommendationPage.PHYSICAL_GOODS -> additionalTrackingData.pgCategories.joinToString(separator = ",")
                                DigitalRecommendationPage.DIGITAL_GOODS -> additionalTrackingData.dgCategories.joinToString(separator = ",")
                                else -> ""
                            }

                    )
            )
            putString(DigitalRecommendationKeys.BUSINESS_UNIT, digitalRecommendationModel.tracking.businessUnit)
            putString(DigitalRecommendationKeys.CURRENT_SITE, DigitalRecommendationValues.CURRENT_SITE)
            putParcelableArrayList(DigitalRecommendationKeys.ITEMS, arrayListOf(
                    Bundle().also {
                        it.putInt(DigitalRecommendationKeys.INDEX, index + 1)
                        it.putString(DigitalRecommendationKeys.ITEM_BRAND, digitalRecommendationModel.tracking.productId)
                        it.putString(DigitalRecommendationKeys.ITEM_CATEGORY, "${digitalRecommendationModel.tracking.categoryId} - ${digitalRecommendationModel.tracking.categoryName}")
                        it.putString(DigitalRecommendationKeys.ITEM_ID, digitalRecommendationModel.tracking.productId)
                        it.putString(DigitalRecommendationKeys.ITEM_NAME, digitalRecommendationModel.unify.title)
                        it.putString(DigitalRecommendationKeys.ITEM_VARIANT, digitalRecommendationModel.tracking.itemType)
                        it.putString(DigitalRecommendationKeys.PRICE, digitalRecommendationModel.tracking.pricePlain)
                    }
            ))
            putString(DigitalRecommendationKeys.USER_ID, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalRecommendationEvents.SELECT_CONTENT, bundle)
    }

    fun impressionDigitalRecommendationThankYouPageItems(
        digitalRecommendationModel: DigitalRecommendationItemUnifyModel,
        additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
        index: Int,
        userId: String,
        page: DigitalRecommendationPage?
    ){
        val data = Bundle().apply {
            putString(DigitalRecommendationKeys.EVENT, DigitalRecommendationEvents.VIEW_ITEM_LIST)
            putString(DigitalRecommendationKeys.EVENT_ACTION, DigitalRecommendationActions.IMPRESSION_THANKYOU_PAGE)
            putString(DigitalRecommendationKeys.EVENT_CATEGORY, getEventCategoryThankYouPage(page))
            putString(DigitalRecommendationKeys.EVENT_LABEL,getEventLabelThankYouPage(
                userType = additionalTrackingData.userType,
                data = digitalRecommendationModel,
                index = index
            ))
            putString(DigitalRecommendationKeys.BUSINESS_UNIT, digitalRecommendationModel.tracking.businessUnit)
            putString(DigitalRecommendationKeys.CURRENT_SITE, DigitalRecommendationValues.CURRENT_SITE)
            putString(DigitalRecommendationKeys.ITEM_LIST, getItemListThankYouPage(digitalRecommendationModel.tracking.itemType))
            putParcelableArrayList(DigitalRecommendationKeys.ITEMS, getItemImpressionsThankYouPage(index, digitalRecommendationModel))
            putString(DigitalRecommendationKeys.USER_ID, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalRecommendationEvents.VIEW_ITEM_LIST, data)
    }

    fun clickDigitalRecommendationThankYouPageItem(
        digitalRecommendationModel: DigitalRecommendationItemUnifyModel,
        additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
        index: Int,
        userId: String,
        page: DigitalRecommendationPage?
    ){
        val data = Bundle().apply {
            putString(DigitalRecommendationKeys.EVENT, DigitalRecommendationEvents.VIEW_ITEM_LIST)
            putString(DigitalRecommendationKeys.EVENT_ACTION, DigitalRecommendationActions.CLICK_THANKYOU_PAGE)
            putString(DigitalRecommendationKeys.EVENT_CATEGORY, getEventCategoryThankYouPage(page))
            putString(DigitalRecommendationKeys.EVENT_LABEL,getEventLabelThankYouPage(
                userType = additionalTrackingData.userType,
                data = digitalRecommendationModel,
                index = index
            ))
            putString(DigitalRecommendationKeys.BUSINESS_UNIT, digitalRecommendationModel.tracking.businessUnit)
            putString(DigitalRecommendationKeys.CURRENT_SITE, DigitalRecommendationValues.CURRENT_SITE)
            putString(DigitalRecommendationKeys.ITEM_LIST, getItemListThankYouPage(digitalRecommendationModel.tracking.itemType))
            putParcelableArrayList(DigitalRecommendationKeys.ITEMS, getItemImpressionsThankYouPage(index, digitalRecommendationModel))
            putString(DigitalRecommendationKeys.USER_ID, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalRecommendationEvents.SELECT_CONTENT, data)
    }

    private fun getItemListThankYouPage(recommendationLogic: String): String{
        return "/ - order complete dg - $recommendationLogic"
    }

    private fun getEventCategoryThankYouPage(page: DigitalRecommendationPage?): String{
        return when (page) {
            DigitalRecommendationPage.DG_THANK_YOU_PAGE -> DigitalRecommendationValues.CATEGORY_THANKYOU_PAGE_DG
            DigitalRecommendationPage.PG_THANK_YOU_PAGE -> DigitalRecommendationValues.CATEGORY_THANKYOU_PAGE_PG
            else -> ""
        }
    }

    /**
     * pattern : {user_loyalty} - {recommendation logic} - {slot_widget_card} - {category_id} - {operator_id} - {product_id} - {price_before} - {price_after} - {campaign_type}
     */
    private fun getEventLabelThankYouPage(
        index: Int,
        userType: String,
        data: DigitalRecommendationItemUnifyModel): String{
        return String.format(
            "%s - %S - %S - %s - %s - %s - %s - %s - %S",
            userType,
            data.tracking.itemType,
            index + 1,
            data.tracking.categoryId,
            data.tracking.operatorId,
            data.tracking.productId,
            data.unify.priceData.slashedPrice,
            data.unify.priceData.price,
            data.unify.campaign.text
        )
    }

    private fun getItemImpressionsThankYouPage(index: Int, data: DigitalRecommendationItemUnifyModel): ArrayList<Bundle>{
        return arrayListOf(Bundle().apply {
            putInt(DigitalRecommendationKeys.INDEX, index + 1)
            putString(DigitalRecommendationKeys.ITEM_BRAND, data.tracking.productId.ifEmpty { "0" })
            putString(DigitalRecommendationKeys.ITEM_CATEGORY, data.tracking.categoryId.ifEmpty { "0" })
            putString(DigitalRecommendationKeys.ITEM_ID, data.tracking.productId.ifEmpty { "0" })
            putString(DigitalRecommendationKeys.ITEM_NAME, data.unify.title)
            putString(DigitalRecommendationKeys.ITEM_VARIANT, data.tracking.itemType)
            putString(DigitalRecommendationKeys.PRICE, "${data.unify.priceData.slashedPrice} - ${data.unify.priceData.price} - ${data.unify.campaign.text}")
        })
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
        const val ITEM_LIST = "item_list"
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
        const val IMPRESSION_THANKYOU_PAGE = "impression on widget recommendation"
        const val IMPRESSION = "impression on widget recommendation dgu"
        const val CLICK_THANKYOU_PAGE = "click on widget recommendation"
        const val CLICK = "click on widget recommendation dgu"
    }
}

class DigitalRecommendationValues {
    companion object {
        const val CATEGORY_PURCHASE_LIST_MP = "my purchase list detail - mp"
        const val CATEGORY_PURCHASE_LIST_DG = "my purchase list detail - dg"

        const val CATEGORY_THANKYOU_PAGE_DG = "order complete - dg"
        const val CATEGORY_THANKYOU_PAGE_PG = "order complete - pg"


        const val CURRENT_SITE = "tokopediadigital"
    }
}