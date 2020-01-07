package com.tokopedia.rechargegeneral.util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.rechargegeneral.util.RechargeGeneralEventTracking.*


/**
 * @author by resakemal on 27/08/19.
 */

class RechargeGeneralAnalytics {

    fun eventClickOperatorClusterDropdown(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_OPERATOR_CLUSTER_DROPDOWN,
                categoryId.toString()
        ))
    }

    fun eventChooseOperatorCluster(categoryId: Int, operatorCluster: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CHOOSE_OPERATOR_CLUSTER,
                "$categoryId - $operatorCluster"
        ))
    }

    fun eventClickOperatorListDropdown(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_OPERATOR_LIST_DROPDOWN,
                categoryId.toString()
        ))
    }

    fun eventChooseOperator(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CHOOSE_OPERATOR,
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickProductListDropdown(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CATEGORY,
                Category.DIGITAL_CATEGORY,
                Action.CLICK_PRODUCT_LIST_DROPDOWN,
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickProductCard(categoryId: Int, operatorId: Int, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_PRODUCT_CARD,
                "$categoryId - $operatorId - $productId"
        ))
    }

    fun eventInputManualNumber(categoryId: Int, operatorId: Int, index: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                "${Action.INPUT_MANUAL_NUMBER} $index",
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickCheckBills(categoryId: Int, operatorId: Int, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_CHECK_BILLS,
                "$categoryId - $operatorId - $productId"
        ))
    }

    fun eventChecklistSusbcriptionBox(categoryId: Int, operatorId: Int, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CHECKLIST_SUBSCRIPTION_BOX,
                "$categoryId - $operatorId - $productId"
        ))
    }

    fun eventCloseInquiry(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_CLOSE_INQUIRY,
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickPromoTab(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_PROMO_TAB,
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickCopyPromo(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_COPY_PROMO,
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickBackButton(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_BACK,
                "$categoryId - $operatorId"
        ))
    }

    fun eventInputFavoriteNumber(categoryId: Int, operatorId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.INPUT_FAVORITE_NUMBER,
                "$categoryId - $operatorId"
        ))
    }

    fun eventClickBuy(categoryId: Int,
                 operatorId: Int,
                 isInstantCheckout: Boolean,
                 enquiryData: TopupBillsEnquiry) {
        val instantCheckoutValue = if (isInstantCheckout) "instant" else "no instant"
        with (enquiryData.attributes) {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            TrackAppUtils.EVENT, Event.ADD_TO_CART,
                            TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_NATIVE,
                            TrackAppUtils.EVENT_ACTION, Action.CLICK_BUY,
                            TrackAppUtils.EVENT_LABEL, "$categoryId - $operatorId - $instantCheckoutValue - $productId",
                            "ecommerce", DataLayer.mapOf(
                                    "currencyCode", "IDR",
                                    "add", DataLayer.mapOf(
                                            "products", DataLayer.listOf(DataLayer.mapOf(
                                                    EnhanceEccomerce.ID, productId,
                                                    EnhanceEccomerce.PRICE, price,
                                                    EnhanceEccomerce.CATEGORY, categoryId,
                                                    EnhanceEccomerce.QUANTITY, 1
                                            ))
                                    )
                            )
                    )
            )
        }
    }

    fun eventClickRecentIcon(recommendationItem: TopupBillsRecommendation,
                        position: Int) {
        with (recommendationItem) {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            TrackAppUtils.EVENT, Event.PRODUCT_CLICK,
                            TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_HOMEPAGE,
                            TrackAppUtils.EVENT_ACTION, Action.CLICK_RECENT_ICON,
                            TrackAppUtils.EVENT_LABEL, "$categoryId - $position",
                            "ecommerce", DataLayer.mapOf(
                                    "click", DataLayer.mapOf(
                                            "actionField", DataLayer.mapOf("list", productId),
                                            "products", DataLayer.listOf(DataLayer.mapOf(
                                                    EnhanceEccomerce.ID, productId,
                                                    EnhanceEccomerce.PRICE, title,
                                                    EnhanceEccomerce.CATEGORY, categoryId,
                                                    EnhanceEccomerce.LIST, productId,
                                                    EnhanceEccomerce.POSITION, position
                                            ))
                                    )
                            )
                    )
            )
        }
    }
}
