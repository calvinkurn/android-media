package com.tokopedia.digital_product_detail.presentation.utils

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_CHEVRON
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_CHEVRON_SHOW_MORE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_CLOSE_BUTTON_PRODUCT_DESC
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_LANJUT_BAYAR
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_LAST_TRANSACTION_ICON
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_LOGIN_WIDGET
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_PRODUCT_CLUSTER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_PROMO_CARD
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.CLICK_PROMO_CLUSTER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.IMPRESSION_LAST_TRANSACTION_ICON
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.IMPRESSION_PDP_BANNER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.IMPRESSION_PRODUCT_CLUSTER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.IMPRESSION_PROMO_CLUSTER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.IMPRESS_PRODUCT_DESC
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.VIEW_PROMO_CARD
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.CATEGORY_ID
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.CREATIVE_NAME
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.CREATIVE_SLOT
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.DIMENSION40
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.DIMENSION45
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.EMPTY_DISCOUNT_PRICE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.FLASH_SALE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.IFRAME_OTP
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.INDEX
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.IS_LOGGEDIN_STATUS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEMS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEM_BRAND
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEM_CATEGORY
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEM_ID
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEM_LIST
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEM_NAME
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.ITEM_VARIANT
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.MCCM
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.PRICE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.PROMOTIONS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.QUANTITY
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.REGULAR
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.SCREEN_NAME
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.SHOP_ID
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.SHOP_NAME
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.SHOP_TYPE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.TRACKER_ID
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.ADD_TO_CART
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.CLICK_DIGITAL
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.OPEN_SCREEN
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.SELECT_CONTENT
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.VIEW_DIGITAL_IRIS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.VIEW_ITEM
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.VIEW_ITEM_LIST
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.CLICK_CHEVRON_PROMOTION
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.CLICK_CLOSE_PRODUCT_DESC
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.CLICK_OLD_MCCM
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.CLICK_PRODUCT_PROMOTION
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.CLICK_SHOW_MORE_PROMOTION
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.IMPRESS_BOTTOM_SHEET_PRODUCT_DESC
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.IMPRESS_OLD_MCCM
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.TrackerId.IMPRESS_PROMO_CLUSTER_PRODUCT
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class DigitalPDPAnalytics {

    fun eventInputNumberManual(categoryName: String, operatorName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.INPUT_MANUAL_NUMBER,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$operatorName"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventInputNumberContact(categoryName: String, operatorName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.INPUT_FROM_CONTACT,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$operatorName"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventInputNumberFavorite(categoryName: String, operatorName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$operatorName"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionFavoriteNumberChips(categoryName: String, loyaltyStatus: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.VIEW_FAVORITE_NUMBER_CHIP,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$loyaltyStatus"
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteNumberChips(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_FAVORITE_NUMBER_CHIP,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionFavoriteContactChips(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.VIEW_FAVORITE_CONTACT_CHIP,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$loyaltyStatus"
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteContactChips(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_FAVORITE_CONTACT_CHIP,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteNumberAutoComplete(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_NUMBER,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteContactAutoComplete(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_CONTACT,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionOmniChannelWidget(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.IMPRESSION_OMNI_CHANNEL_WIDGET,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.IMPRESSION_OMNI_CHANNEL_WIDGET
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickOmniChannelWidget(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_OMNI_CHANNEL_WIDGET,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.CLICK_OMNI_CHANNEL_WIDGET
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43697
    fun impressionCheckBalanceWidget(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.IMPRESSION_CHECK_BALANCE_WIDGET,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $operatorName - $loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.IMPRESSION_CHECK_BALANCE_WIDGET
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43698
    fun clickCheckBalanceWidget(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_CHECK_BALANCE_WIDGET,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $operatorName - $loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.CLICK_CHECK_BALANCE_WIDGET
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43699
    fun impressionPdpOtpPopUp(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.IMPRESSION_PDP_OTP_POP_UP,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.IMPRESSION_PDP_OTP_POP_UP
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43700
    fun clickPdpOtpPopUp(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_PDP_OTP_POP_UP,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.CLICK_PDP_OTP_TOP_UP
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43701
    fun clickInputOtp(
        userId: String,
        isLogin: Boolean
    ) {
        val data = DataLayer.mapOf(
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.CLICK_INPUT_OTP,
            IS_LOGGEDIN_STATUS,
            isLogin,
            SCREEN_NAME,
            IFRAME_OTP
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43702
    fun clickCloseOtp(
        categoryName: String,
        loyaltyStatus: String,
        userId: String,
        isLogin: Boolean
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_CLOSE_OTP,
            TrackAppUtils.EVENT_LABEL, "$categoryName - $loyaltyStatus",
            TRACKER_ID, DigitalPDPEventTracking.TrackerId.CLICK_CLOSE_OTP,
            IS_LOGGEDIN_STATUS, isLogin,
            SCREEN_NAME, IFRAME_OTP
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43704
    fun impressionCheckBalanceInfo(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        errorMessage: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.IMPRESSION_CHECK_BALANCE_INFO,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $operatorName - $loyaltyStatus - $errorMessage",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.IMPRESSION_CHECK_BALANCE_INFO
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43705
    fun clickLihatDetailCheckBalance(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_LIHAT_DETAIL_CHECK_BALANCE,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $operatorName - $loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.CLICK_LIHAT_DETAIL_CHECK_BALANCE
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43706
    fun impressionSheetActivePackage(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.IMPRESSION_SHEET_ACTIVE_PACKAGE)
            putString(TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $loyaltyStatus")
            putString(TRACKER_ID, DigitalPDPEventTracking.TrackerId.IMPRESSION_SHEET_ACTIVE_PACKAGE)
            putParcelableArrayList(
                ITEMS,
                mapperCheckBalanceProductToItemList(
                    categoryName,
                    operatorName,
                    "",
                    "",
                    ""
                )
            )
        }
        eventDataLayer.viewItem(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43707
    fun clickCloseSheetActivePackage(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_CLOSE_SHEET_ACTIVE_PACKAGE,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName - $operatorName - $loyaltyStatus",
            TRACKER_ID,
            DigitalPDPEventTracking.TrackerId.CLICK_CLOSE_SHEET_ACTIVE_PACKAGE
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43708
    fun impressionActivePackageCheckBalance(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        productListName: String,
        productId: String,
        productName: String,
        index: Int,
        price: String,
        userId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.IMPRESSION_ACTIVE_PACKAGE_CHECK_BALANCE)
            putString(TrackAppUtils.EVENT_LABEL, "$categoryName - $index - $operatorName - $loyaltyStatus")
            putString(TRACKER_ID, DigitalPDPEventTracking.TrackerId.IMPRESSION_ACTIVE_PACKAGE_CHECK_BALANCE)
            putString(ITEM_LIST, productListName)
            putParcelableArrayList(
                ITEMS,
                mapperCheckBalanceProductToDimen40ItemList(
                    productListName,
                    index,
                    categoryName,
                    operatorName,
                    productId,
                    productName,
                    price
                )
            )
        }
        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3960
    // Tracker ID: 43709
    fun clickBeliLagiActivePackage(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        productListName: String,
        productId: String,
        productName: String,
        index: Int,
        price: String,
        userId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_BELI_LAGI_ACTIVE_PACKAGE)
            putString(TrackAppUtils.EVENT_LABEL, "$categoryName - $index - $operatorName - $loyaltyStatus")
            putString(TRACKER_ID, DigitalPDPEventTracking.TrackerId.CLICK_BELI_LAGI_ACTIVE_PACKAGE)
            putString(ITEM_LIST, productListName)
            putParcelableArrayList(
                ITEMS,
                mapperCheckBalanceProductToDimen40ItemList(
                    productListName,
                    index,
                    categoryName,
                    operatorName,
                    productId,
                    productName,
                    price
                )
            )
        }
        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun eventClearInputNumber(categoryName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLEAR_INPUT_NUMBER,
            TrackAppUtils.EVENT_LABEL,
            categoryName
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickOnContactIcon(categoryName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_ON_CONTACT_ICON,
            TrackAppUtils.EVENT_LABEL,
            categoryName
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionProductCluster(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        position: Int
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, IMPRESSION_PRODUCT_CLUSTER)
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_$loyaltyStatus")
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, "", categoryName)
            )
        }

        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    fun clickProductCluster(
        productListName: String,
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        position: Int
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_PRODUCT_CLUSTER)
            putString(ITEM_LIST, productListName)
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_$loyaltyStatus")
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, "", categoryName)
            )
        }

        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressionLastTransactionIcon(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        recomData: RecommendationCardWidgetModel,
        position: Int
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, IMPRESSION_LAST_TRANSACTION_ICON)
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}_${recomData.itemType}")
            putParcelableArrayList(
                ITEMS,
                mapperRecommendationToItemList(recomData, operatorName, position)
            )
        }

        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    fun clickLastTransactionIcon(
        productListName: String,
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        recomData: RecommendationCardWidgetModel,
        position: Int
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_LAST_TRANSACTION_ICON)
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}_${position}_${recomData.itemType}")
            putString(ITEM_LIST, productListName)
            putParcelableArrayList(
                ITEMS,
                mapperRecommendationToItemList(recomData, operatorName, position)
            )
        }

        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressionBannerEmptyState(
        creativeUrl: String,
        categoryId: String,
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, IMPRESSION_PDP_BANNER)
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_$loyaltyStatus")
            putParcelableArrayList(PROMOTIONS, mapperBannerToItemList(creativeUrl, categoryId, categoryName))
        }

        eventDataLayer.viewItem(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    fun clickLoginWidget(categoryName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            CLICK_LOGIN_WIDGET,
            TrackAppUtils.EVENT_LABEL,
            "$categoryName"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionProductMCCMGrid(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        denomType: DenomWidgetEnum,
        position: Int

    ) {
        val isMCCMorFlashSale =
            if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE
            ) {
                MCCM
            } else {
                FLASH_SALE
            }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, VIEW_PROMO_CARD)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${categoryName}_${operatorName}_${isMCCMorFlashSale}_${loyaltyStatus}_${denomData.itemType}"
            )
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale, categoryName)
            )
            putString(TRACKER_ID, IMPRESS_OLD_MCCM)
        }

        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    fun impressMCCMProductFullVertical(
        productListName: String,
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        denomType: DenomWidgetEnum,
        position: Int
    ) {
        val isMCCMorFlashSale =
            if (denomType == DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE) {
                MCCM
            } else {
                FLASH_SALE
            }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, IMPRESSION_PROMO_CLUSTER)
            putString(ITEM_LIST, productListName)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$categoryName - $operatorName - ${(position + Int.ONE)} - ${denomData.id} - $loyaltyStatus"
            )
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale, categoryName, productListName)
            )
            putString(TRACKER_ID, IMPRESS_PROMO_CLUSTER_PRODUCT)
        }

        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    fun clickChevronBuyWidget(
        categoryName: String,
        operatorName: String,
        discountPrice: String,
        normalPrice: String,
        userId: String
    ) {
        val finalNormalPrice = if (normalPrice.isNullOrEmpty()) discountPrice else normalPrice
        val finalDiscountPrice =
            if (normalPrice.isNullOrEmpty()) EMPTY_DISCOUNT_PRICE else discountPrice
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            CLICK_CHEVRON,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_${finalNormalPrice}_$finalDiscountPrice"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickMCCMGridProduct(
        productListName: String,
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        denomType: DenomWidgetEnum,
        position: Int
    ) {
        val isMCCMorFlashSale =
            if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE
            ) {
                MCCM
            } else {
                FLASH_SALE
            }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_PROMO_CARD)
            putString(ITEM_LIST, productListName)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${categoryName}_${operatorName}_${isMCCMorFlashSale}_${loyaltyStatus}_${denomData.itemType}"
            )
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale, categoryName)
            )
            putString(TRACKER_ID, CLICK_OLD_MCCM)
        }

        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun clickMCCMProductFullVertical(
        productListName: String,
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        denomType: DenomWidgetEnum,
        position: Int
    ) {
        val isMCCMorFlashSale =
            if (denomType == DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE) {
                MCCM
            } else {
                FLASH_SALE
            }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_PROMO_CLUSTER)
            putString(ITEM_LIST, productListName)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$categoryName - $operatorName - ${(position + Int.ONE)} - ${denomData.id} - $loyaltyStatus"
            )
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale, categoryName, productListName)
            )
            putString(TRACKER_ID, CLICK_PRODUCT_PROMOTION)
        }

        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressionFilterChip(
        categoryName: String,
        operatorName: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.IMPRESSION_FILTER_CHIP,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$operatorName"
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFilterChip(
        categoryName: String,
        operatorName: String,
        filterChipName: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_FILTER_CHIP,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$filterChipName"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFullDenomChevron(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_CHEVRON_IN_PRODUCT_CLUSTER,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickPromoFullDenomChevron(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        productListName: String,
        denomData: DenomData,
        position: Int,
        denomType: DenomWidgetEnum
    ) {
        val isMCCMorFlashSale =
            if (denomType == DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE) {
                MCCM
            } else {
                FLASH_SALE
            }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_CHEVRON_IN_PROMO_CARD)
            putString(ITEM_LIST, productListName)
            putString(TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $loyaltyStatus")
            putString(TRACKER_ID, CLICK_CHEVRON_PROMOTION)
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale, categoryName, productListName)
            )
        }

        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun clickTransactionHistoryIcon(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_TRANSACTION_HISTORY_ICON,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickScanBarcode(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_SCAN_BARCODE,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickTransactionDetailInfo(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_TRANSACTION_DETAIL_INFO,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickListFavoriteNumber(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.CLICK_LIST_FAVORITE_NUMBER,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_$loyaltyStatus"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionGreenBox(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        title: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            "impression '$title'",
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_$loyaltyStatus"
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun addToCart(
        categoryId: String,
        categoryName: String,
        operatorName: String,
        userId: String,
        cartId: String,
        productId: String,
        productName: String,
        price: String,
        channelId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_LANJUT_BAYAR)
            putString(TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $channelId")
            putParcelableArrayList(
                ITEMS,
                mapperAtcToItemList(
                    categoryId,
                    categoryName,
                    cartId,
                    operatorName,
                    productId,
                    productName,
                    price
                )
            )
        }

        eventDataLayer.clickATCGeneralTracker(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART, eventDataLayer)
    }

    fun viewPDPPage(
        categoryName: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            DigitalPDPEventTracking.Action.VIEW_PDP_PAGE,
            TrackAppUtils.EVENT_LABEL,
            categoryName
        )
        data.viewDigitalIris(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun openScreenPDPPage(
        categoryName: String,
        userId: String,
        isLogin: Boolean
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, OPEN_SCREEN)
            putString(
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT,
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE
            )
            putString(
                DigitalPDPEventTracking.Additional.CURRENT_SITE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE
            )
            putString(DigitalPDPEventTracking.Additional.USER_ID, userId)
            putString(DigitalPDPEventTracking.Additional.SCREEN_NAME, "/digital/$categoryName")
            putString(DigitalPDPEventTracking.Additional.IS_LOGGEDIN_STATUS, "$isLogin")
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, eventDataLayer)
    }

    fun clickExpandMCCM(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_CHEVRON_SHOW_MORE)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$categoryName - $operatorName - $loyaltyStatus"
            )
            putString(TRACKER_ID, CLICK_SHOW_MORE_PROMOTION)
        }

        eventDataLayer.clickDigitalGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CLICK_DIGITAL, eventDataLayer)
    }

    fun clickCloseProductDescription(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomType: DenomWidgetEnum
    ) {
        val recommendationLogic = if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE ||
            denomType == DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE
        ) {
            MCCM
        } else if (
            denomType == DenomWidgetEnum.GRID_TYPE ||
            denomType == DenomWidgetEnum.FULL_TYPE
        ) {
            REGULAR
        } else {
            FLASH_SALE
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_CLOSE_BUTTON_PRODUCT_DESC)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$categoryName - $operatorName - $recommendationLogic - $loyaltyStatus"
            )
            putString(TRACKER_ID, CLICK_CLOSE_PRODUCT_DESC)
        }

        eventDataLayer.clickDigitalGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CLICK_DIGITAL, eventDataLayer)
    }

    fun impressProductDescription(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomType: DenomWidgetEnum,
        denomData: DenomData,
        productListTitle: String,
        position: Int
    ) {
        val recommendationLogic = if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE ||
            denomType == DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE
        ) {
            MCCM
        } else if (
            denomType == DenomWidgetEnum.GRID_TYPE ||
            denomType == DenomWidgetEnum.FULL_TYPE
        ) {
            REGULAR
        } else {
            FLASH_SALE
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, IMPRESS_PRODUCT_DESC)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$categoryName - $operatorName - $recommendationLogic - $loyaltyStatus"
            )
            putString(TRACKER_ID, IMPRESS_BOTTOM_SHEET_PRODUCT_DESC)
            putString(ITEM_LIST, productListTitle)
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, recommendationLogic, categoryName, productListTitle)
            )
        }

        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    /** Common Tracking extension function*/

    fun Bundle.viewItemList(userId: String): Bundle {
        addGeneralTracker(userId)
        this.putString(TrackAppUtils.EVENT, VIEW_ITEM_LIST)
        return this
    }

    fun Bundle.viewItem(userId: String): Bundle {
        addGeneralTracker(userId)
        this.putString(TrackAppUtils.EVENT, VIEW_ITEM)
        return this
    }

    fun MutableMap<String, Any>.viewDigitalIris(userId: String): MutableMap<String, Any> {
        addGeneralTracker(userId)
        this[TrackAppUtils.EVENT] = VIEW_DIGITAL_IRIS
        return this
    }

    fun Bundle.clickGeneralItemList(userId: String): Bundle {
        addGeneralTracker(userId)
        this.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        return this
    }

    fun Bundle.clickDigitalGeneralItemList(userId: String): Bundle {
        addGeneralTracker(userId)
        this.putString(TrackAppUtils.EVENT, CLICK_DIGITAL)
        return this
    }

    fun Bundle.clickATCGeneralTracker(userId: String): Bundle {
        addGeneralATCTracker(userId)
        this.putString(TrackAppUtils.EVENT, ADD_TO_CART)
        return this
    }

    fun MutableMap<String, Any>.clickDigitalItemList(userId: String): MutableMap<String, Any> {
        addGeneralTracker(userId)
        this[TrackAppUtils.EVENT] = CLICK_DIGITAL
        return this
    }

    fun Bundle.addGeneralATCTracker(userId: String): Bundle {
        this.putString(
            TrackAppUtils.EVENT_CATEGORY,
            DigitalPDPEventTracking.Category.DIGITAL_NATIVE
        )
        this.putString(
            DigitalPDPEventTracking.Additional.BUSINESS_UNIT,
            DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE
        )
        this.putString(
            DigitalPDPEventTracking.Additional.CURRENT_SITE,
            DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE
        )
        this.putString(DigitalPDPEventTracking.Additional.USER_ID, userId)
        return this
    }

    fun Bundle.addGeneralTracker(userId: String): Bundle {
        this.putString(
            TrackAppUtils.EVENT_CATEGORY,
            DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE
        )
        this.putString(
            DigitalPDPEventTracking.Additional.BUSINESS_UNIT,
            DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE
        )
        this.putString(
            DigitalPDPEventTracking.Additional.CURRENT_SITE,
            DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE
        )
        this.putString(DigitalPDPEventTracking.Additional.USER_ID, userId)
        return this
    }

    fun MutableMap<String, Any>.addGeneralTracker(userId: String): MutableMap<String, Any> {
        this[TrackAppUtils.EVENT_CATEGORY] = DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE
        this[DigitalPDPEventTracking.Additional.BUSINESS_UNIT] =
            DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE
        this[DigitalPDPEventTracking.Additional.CURRENT_SITE] =
            DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE
        this[DigitalPDPEventTracking.Additional.USER_ID] = userId
        return this
    }

    /** Tracking mapper*/
    fun mapperDenomToItemList(
        denomData: DenomData,
        operatorName: String,
        position: Int,
        isMCCMorFlashSale: String,
        categoryName: String,
        productListName: String = ""
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        denomData.run {
            listItems.add(
                Bundle().apply {
                    putString(INDEX, (position + 1).toString())
                    putString(ITEM_BRAND, operatorName)
                    putString(
                        ITEM_CATEGORY,
                        categoryName
                    )
                    putString(ITEM_ID, denomData.id)
                    putString(ITEM_NAME, denomData.title)
                    putString(ITEM_VARIANT, isMCCMorFlashSale)
                    putString(PRICE, denomData.price)
                    if (productListName.isNotEmpty()) {
                        putString(DIMENSION40, productListName)
                    }
                }
            )
        }
        return listItems
    }

    fun mapperRecommendationToItemList(
        recomData: RecommendationCardWidgetModel,
        operatorName: String,
        position: Int
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        recomData.run {
            listItems.add(
                Bundle().apply {
                    putString(INDEX, (position + 1).toString())
                    putString(ITEM_BRAND, operatorName)
                    putString(
                        ITEM_CATEGORY,
                        DigitalPDPCategoryUtil.getCategoryName(recomData.categoryId.toIntSafely())
                    )
                    putString(ITEM_ID, recomData.id)
                    putString(ITEM_NAME, recomData.title)
                    putString(ITEM_VARIANT, "-")
                    putString(PRICE, recomData.price)
                }
            )
        }
        return listItems
    }

    fun mapperBannerToItemList(
        creativeUrl: String,
        categoryId: String,
        categoryName: String
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        listItems.add(
            Bundle().apply {
                putString(CREATIVE_NAME, creativeUrl)
                putString(CREATIVE_SLOT, "-")
                putString(ITEM_ID, categoryId)
                putString(ITEM_NAME, categoryName)
            }
        )
        return listItems
    }

    fun mapperAtcToItemList(
        categoryId: String,
        categoryName: String,
        cartId: String,
        operatorName: String,
        productId: String,
        productName: String,
        price: String
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        listItems.add(
            Bundle().apply {
                putString(CATEGORY_ID, categoryId)
                putString(DIMENSION45, cartId)
                putString(ITEM_BRAND, operatorName)
                putString(ITEM_CATEGORY, categoryName)
                putString(ITEM_ID, productId)
                putString(ITEM_NAME, productName)
                putString(ITEM_VARIANT, "")
                putString(PRICE, price)
                putString(QUANTITY, "1")
                putString(SHOP_ID, null)
                putString(SHOP_NAME, null)
                putString(SHOP_TYPE, null)
            }
        )
        return listItems
    }

    fun mapperCheckBalanceProductToDimen40ItemList(
        title: String,
        index: Int,
        categoryName: String,
        operatorName: String,
        productId: String,
        productName: String,
        price: String
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        listItems.add(
            Bundle().apply {
                putString(DIMENSION40, title)
                putString(INDEX, index.toString())
                putString(ITEM_BRAND, operatorName)
                putString(ITEM_CATEGORY, categoryName)
                putString(ITEM_ID, productId)
                putString(ITEM_NAME, productName)
                putString(ITEM_VARIANT, "")
                putString(PRICE, price)
            }
        )
        return listItems
    }

    fun mapperCheckBalanceProductToItemList(
        categoryName: String,
        operatorName: String,
        productId: String,
        productName: String,
        price: String
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        listItems.add(
            Bundle().apply {
                putString(ITEM_BRAND, operatorName)
                putString(ITEM_CATEGORY, categoryName)
                putString(ITEM_ID, productId)
                putString(ITEM_NAME, productName)
                putString(ITEM_VARIANT, "")
                putString(PRICE, price)
            }
        )
        return listItems
    }
}
