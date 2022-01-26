package com.tokopedia.digital_product_detail.presentation.utils

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.CLICK_CHEVRON
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.CLICK_LAST_TRANSACTION_ICON
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.CLICK_LOGIN_WIDGET
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.CLICK_PRODUCT_CLUSTER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.CLICK_PROMO_CARD
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.IMPRESSION_LAST_TRANSACTION_ICON
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.IMPRESSION_PDP_BANNER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.IMPRESSION_PRODUCT_CLUSTER
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Action.Companion.VIEW_PROMO_CARD
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.CREATIVE_NAME
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.CREATIVE_SLOT
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.EMPTY_DISCOUNT_PRICE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.FLASH_SALE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.INDEX
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEMS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEM_BRAND
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEM_CATEGORY
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEM_ID
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEM_LIST
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEM_NAME
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.ITEM_VARIANT
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.MCCM
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.PRICE
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Additional.Companion.PROMOTIONS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.Companion.CLICK_DIGITAL
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.Companion.SELECT_CONTENT
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.Companion.VIEW_DIGITAL_IRIS
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.Companion.VIEW_ITEM
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPEventTracking.Event.Companion.VIEW_ITEM_LIST
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class DigitalPDPTelcoAnalytics {

    fun eventInputNumberManual(categoryName: String, operatorName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.INPUT_MANUAL_NUMBER,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventInputNumberContact(categoryName: String, operatorName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.INPUT_FROM_CONTACT,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventInputNumberFavorite(categoryName: String, operatorName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionFavoriteNumberChips(categoryName: String, loyaltyStatus: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.VIEW_FAVORITE_NUMBER_CHIP,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${loyaltyStatus}"
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
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_FAVORITE_NUMBER_CHIP,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}_${loyaltyStatus}"
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
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.VIEW_FAVORITE_CONTACT_CHIP,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${loyaltyStatus}"
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
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_FAVORITE_CONTACT_CHIP,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}_${loyaltyStatus}"
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
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_NUMBER,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}_${loyaltyStatus}"
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
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_CONTACT,
            TrackAppUtils.EVENT_LABEL,  "${categoryName}_${operatorName}_${loyaltyStatus}"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClearInputNumber(categoryName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLEAR_INPUT_NUMBER,
            TrackAppUtils.EVENT_LABEL, categoryName
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickOnContactIcon(categoryName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_ON_CONTACT_ICON,
            TrackAppUtils.EVENT_LABEL, categoryName
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
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}")
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, "")
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
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}")
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, "")
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
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}")
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
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}_${position}")
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
        creativeUrl: String, categoryId: String, categoryName: String,
        loyaltyStatus: String, userId: String
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, IMPRESSION_PDP_BANNER)
            putString(TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}")
            putParcelableArrayList(PROMOTIONS, mapperBannerToItemList(creativeUrl, categoryId, categoryName))
        }

        eventDataLayer.viewItem(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)

    }

    fun clickLoginWidget(categoryName: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, CLICK_LOGIN_WIDGET,
            TrackAppUtils.EVENT_LABEL, "${categoryName}"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionProductMCCM(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String,
        denomData: DenomData,
        denomType: DenomWidgetEnum,
        position: Int

    ) {
        val isMCCMorFlashSale =
            if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE) MCCM else FLASH_SALE
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, VIEW_PROMO_CARD)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${categoryName}_${operatorName}_${isMCCMorFlashSale}_${loyaltyStatus}"
            )
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale)
            )
        }

        eventDataLayer.viewItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventDataLayer)
    }

    fun clickChevronBuyWidget(
        categoryName: String, operatorName: String, discountPrice: String,
        normalPrice: String, userId: String
    ) {
        val finalNormalPrice = if (normalPrice.isNullOrEmpty()) discountPrice else normalPrice
        val finalDiscountPrice =
            if (normalPrice.isNullOrEmpty()) EMPTY_DISCOUNT_PRICE else discountPrice
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            CLICK_CHEVRON,
            TrackAppUtils.EVENT_LABEL,
            "${categoryName}_${operatorName}_${finalNormalPrice}_${finalDiscountPrice}"
        )
        data.clickDigitalItemList(userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickMCCMProduct(
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
            if (denomType == DenomWidgetEnum.MCCM_GRID_TYPE) MCCM else FLASH_SALE
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_PROMO_CARD)
            putString(ITEM_LIST, productListName)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${categoryName}_${operatorName}_${isMCCMorFlashSale}_${loyaltyStatus}"
            )
            putParcelableArrayList(
                ITEMS,
                mapperDenomToItemList(denomData, operatorName, position, isMCCMorFlashSale)
            )
        }

        eventDataLayer.clickGeneralItemList(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    /** Common Tracking Methods*/

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

    fun MutableMap<String, Any>.clickDigitalItemList(userId: String): MutableMap<String, Any> {
        addGeneralTracker(userId)
        this[TrackAppUtils.EVENT] = CLICK_DIGITAL
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
        isMCCMorFlashSale: String
    ): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        denomData.run {
            listItems.add(
                Bundle().apply {
                    putString(INDEX, (position + 1).toString())
                    putString(ITEM_BRAND, operatorName)
                    putString(
                        ITEM_CATEGORY,
                        DigitalPDPTelcoUtil.getCategoryName(denomData.categoryId.toInt())
                    )
                    putString(ITEM_ID, denomData.id)
                    putString(ITEM_NAME, denomData.title)
                    putString(ITEM_VARIANT, isMCCMorFlashSale)
                    putString(PRICE, denomData.price)
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
                        DigitalPDPTelcoUtil.getCategoryName(recomData.categoryId.toInt())
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
}