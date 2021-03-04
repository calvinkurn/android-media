package com.tokopedia.rechargegeneral.util

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.rechargegeneral.util.RechargeGeneralEventTracking.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by resakemal on 27/08/19.
 */

class RechargeGeneralAnalytics {

    fun eventClickOperatorClusterDropdown(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_OPERATOR_CLUSTER_DROPDOWN,
                categoryName
        )
    }

    fun eventChooseOperatorCluster(categoryName: String, operatorCluster: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CHOOSE_OPERATOR_CLUSTER,
                "$categoryName - $operatorCluster"
        )
    }

    fun eventClickOperatorListDropdown(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_OPERATOR_LIST_DROPDOWN,
                categoryName
        )
    }

    fun eventChooseOperator(categoryName: String, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CHOOSE_OPERATOR,
                "$categoryName - $operatorName"
        )
    }

    fun eventClickProductListDropdown(categoryName: String, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_CATEGORY,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_PRODUCT_LIST_DROPDOWN,
                "$categoryName - $operatorName"
        )
    }

    fun eventClickProductCard(categoryName: String, operatorName: String, productName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_PRODUCT_CARD,
                "$categoryName - $operatorName - $productName"
        )
    }

    fun eventInputManualNumber(categoryName: String, operatorName: String, index: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                "${Action.INPUT_MANUAL_NUMBER} $index",
                "$categoryName - $operatorName"
        )
    }

    fun eventClickCheckBills(categoryName: String, operatorName: String, productName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_CHECK_BILLS,
                "$categoryName - $operatorName - $productName"
        )
    }

    fun eventChecklistSusbcriptionBox(categoryName: String, operatorName: String, productName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CHECKLIST_SUBSCRIPTION_BOX,
                "$categoryName - $operatorName - $productName"
        )
    }

    fun eventCloseInquiry(categoryName: String, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_CLOSE_INQUIRY,
                "$categoryName - $operatorName"
        )
    }

    fun eventClickPromoTab(categoryName: String, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_PROMO_TAB,
                "$categoryName - $operatorName"
        )
    }

    fun eventClickCopyPromo(promoName: String, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_COPY_PROMO,
                "$promoName - $position"
        )
    }

    fun eventClickBackButton(categoryName: String, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_BACK,
                "$categoryName - $operatorName"
        )
    }

    fun eventInputFavoriteNumber(categoryName: String, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.INPUT_FAVORITE_NUMBER,
                "$categoryName - $operatorName"
        )
    }

    fun eventClickBuy(categoryName: String,
                 operatorName: String,
                 isInstantCheckout: Boolean,
                 enquiryData: TopupBillsEnquiry) {
        enquiryData.attributes?.run {
            val instantCheckoutValue = if (isInstantCheckout) "instant" else "no instant"
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            TrackAppUtils.EVENT, Event.ADD_TO_CART,
                            TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_NATIVE,
                            TrackAppUtils.EVENT_ACTION, Action.CLICK_BUY,
                            TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $instantCheckoutValue - $productId",
                            "ecommerce", DataLayer.mapOf(
                                    "currencyCode", "IDR",
                                    "add", DataLayer.mapOf(
                                            "products", DataLayer.listOf(DataLayer.mapOf(
                                                    EnhanceEccomerce.ID, productId,
                                                    EnhanceEccomerce.PRICE, price,
                                                    EnhanceEccomerce.CATEGORY, categoryName,
                                                    EnhanceEccomerce.QUANTITY, 1
                                            ))
                                    )
                            )
                    )
            )
        }
    }

    fun eventClickRecentIcon(recommendationItem: TopupBillsRecommendation,
                             categoryName: String,
                             position: Int) {
        with (recommendationItem) {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            TrackAppUtils.EVENT, Event.PRODUCT_CLICK,
                            TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_HOMEPAGE,
                            TrackAppUtils.EVENT_ACTION, Action.CLICK_RECENT_ICON,
                            TrackAppUtils.EVENT_LABEL, "$categoryName - $position",
                            "ecommerce", DataLayer.mapOf(
                                    "click", DataLayer.mapOf(
                                            "actionField", DataLayer.mapOf("list", productId),
                                            "products", DataLayer.listOf(DataLayer.mapOf(
                                                    EnhanceEccomerce.ID, productId,
                                                    EnhanceEccomerce.PRICE, title,
                                                    EnhanceEccomerce.CATEGORY, categoryName,
                                                    EnhanceEccomerce.LIST, productId,
                                                    EnhanceEccomerce.POSITION, position
                                            ))
                                    )
                            )
                    )
            )
        }
    }

    fun onClickSliceRecharge(userId: String, rechargeProductFromSlice: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, "clickGAMain",
                TrackAppUtils.EVENT_CATEGORY, "ga main app",
                TrackAppUtils.EVENT_ACTION, "click item transaction",
                TrackAppUtils.EVENT_LABEL, rechargeProductFromSlice,
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ))
    }

    fun onOpenPageFromSlice() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, "openScreen",
                EVENT_SCREEN_NAME, "recharge general - from voice search - mainapp"
        ))
    }

    companion object{
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val USER_ID = "userId"
        const val EVENT_SCREEN_NAME = "screenName"
    }
}
