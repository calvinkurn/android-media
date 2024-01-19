package com.tokopedia.shopdiscount.utils.tracker

import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CAMPAIGN_BUSINESS_UNIT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CLICK_ADD_PRODUCT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CLICK_PG
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CLICK_SAVE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CREATE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EDIT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_CLOSE_BOTTOM_SHEET
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_CLOSE_BOTTOM_SHEET_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_CTA
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_CTA_OPT_OUT_PARENT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_CTA_OPT_OUT_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_EDU_ARTICLE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_OPT_OUT_BULK
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_OPT_OUT_NON_BULK
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_OPT_OUT_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.CLICK_SUBSIDY_INFORMATION
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.IMPRESSION_COACH_MARK
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.IMPRESSION_NON_EDITABLE_PARENT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.IMPRESSION_NON_EDITABLE_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.IMPRESSION_OPT_OUT_REASON_BOTTOM_SHEET_NO_SUBSIDY_PRODUCT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventAction.IMPRESSION_SUBSIDY_DETAIL
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventCategory.SLASH_PRICE_SUBSIDY_BOTTOM_SHEET
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventCategory.SLASH_PRICE_SUBSIDY_LIST_OF_PRODUCTS
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventCategory.SLASH_PRICE_SUBSIDY_OPT_OUT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventLabel.SLASH_PRICE_SUBSIDY_PAGE_SOURCE_BOTTOM_SHEET
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventLabel.SLASH_PRICE_SUBSIDY_PAGE_SOURCE_LIST_OF_PRODUCTS
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventLabel.SLASH_PRICE_SUBSIDY_NON_VARIANT_PRODUCT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EventLabel.SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.Key.TRACKER_ID
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.PHYSICAL_GOODS_BUSINESS_UNIT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.SLASH_PRICE_LIST_OF_PRODUCTS
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.SLASH_PRICE_SET_DISCOUNT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TOKOPEDIA_SELLER
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_CLOSE_BOTTOM_SHEET_OPT_OUT_REASON
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_CLOSE_BOTTOM_SHEET_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_EDU_ARTICLE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_CTA_OPT_OUT_PARENT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_CTA_OPT_OUT_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_EDU_ARTICLE_OPT_OUT_BOTTOM_SHEET
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_OPT_OUT_BULK
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_OPT_OUT_NON_BULK
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_OPT_OUT_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_SUBMIT_OPT_OUT_REASON
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_SUBSIDY_INFORMATION_BOTTOM_SHEET
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_CLICK_SUBSIDY_INFORMATION_PRODUCT_LIST
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_IMPRESSION_COACH_MARK_BOTTOM_SHEET
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_IMPRESSION_COACH_MARK_PRODUCT_LIST
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_IMPRESSION_NON_EDITABLE_PARENT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_IMPRESSION_NON_EDITABLE_VARIANT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_IMPRESSION_OPT_OUT_REASON_BOTTOM_SHEET_NO_SUBSIDY_PRODUCT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TrackerId.TRACKER_ID_IMPRESSION_SUBSIDY_DETAIL
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.VIEW_PG_IRIS
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Tracker reference for slash price subsidy feature https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4451
 */
class ShopDiscountTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickAddProductEvent() {
        Tracker.Builder()
            .setBusinessUnit(PHYSICAL_GOODS_BUSINESS_UNIT)
            .setEventCategory(SLASH_PRICE_LIST_OF_PRODUCTS)
            .setCurrentSite(TOKOPEDIA_SELLER)
            .setShopId(userSession.shopId)
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_ADD_PRODUCT)
            .setEventLabel(EMPTY_STRING)
            .build()
            .send()
    }

    fun sendClickSaveSlashPrice(mode: String) {
        val eventCategory = String.format(
            SLASH_PRICE_SET_DISCOUNT,
            getSlashPriceSetType(mode)
        )
        Tracker.Builder()
            .setBusinessUnit(PHYSICAL_GOODS_BUSINESS_UNIT)
            .setEventCategory(eventCategory)
            .setCurrentSite(TOKOPEDIA_SELLER)
            .setShopId(userSession.shopId)
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_SAVE)
            .setEventLabel(EMPTY_STRING)
            .build()
            .send()
    }

    private fun getSlashPriceSetType(mode: String): String {
        return when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                CREATE
            }

            else -> {
                EDIT
            }
        }
    }

    fun sendImpressionSlashPriceSubsidyCoachMarkListProductEvent(
        hasProductVariant: Boolean,
        productId: String
    ) {
        val eventLabel = if (hasProductVariant) {
            SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT
        } else {
            SLASH_PRICE_SUBSIDY_NON_VARIANT_PRODUCT
        }.format(productId)
        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_COACH_MARK)
            .setEventCategory(SLASH_PRICE_SUBSIDY_LIST_OF_PRODUCTS)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_COACH_MARK_PRODUCT_LIST)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressionSlashPriceSubsidyCoachMarkBottomSheetEvent(productId: String) {
        val eventLabel = SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT.format(productId)
        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_COACH_MARK)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_COACH_MARK_BOTTOM_SHEET)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendSlashPriceClickSubsidyInformationListProductEvent(hasVariant: Boolean, id: String) {
        val eventLabel = if (hasVariant) {
            SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT
        } else {
            SLASH_PRICE_SUBSIDY_NON_VARIANT_PRODUCT
        }.format(id)
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_SUBSIDY_INFORMATION)
            .setEventCategory(SLASH_PRICE_SUBSIDY_LIST_OF_PRODUCTS)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_SUBSIDY_INFORMATION_PRODUCT_LIST)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendSlashPriceClickSubsidyInformationBottomSheetEvent(
        hasVariant: Boolean,
        id: String
    ) {
        val eventLabel = if (hasVariant) {
            SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT
        } else {
            SLASH_PRICE_SUBSIDY_NON_VARIANT_PRODUCT
        }.format(id)
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_SUBSIDY_INFORMATION)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_SUBSIDY_INFORMATION_BOTTOM_SHEET)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressionSubsidyProgramInformationBottomSheetEvent(
        isBottomSheet: Boolean,
        productId: String
    ) {
        val eventLabel = if (isBottomSheet) {
            SLASH_PRICE_SUBSIDY_PAGE_SOURCE_BOTTOM_SHEET
        } else {
            SLASH_PRICE_SUBSIDY_PAGE_SOURCE_LIST_OF_PRODUCTS
        }.format(productId)
        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_SUBSIDY_DETAIL)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_SUBSIDY_DETAIL)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickEduArticleEvent(isBottomSheet: Boolean, productId: String) {
        val eventLabel = if (isBottomSheet) {
            SLASH_PRICE_SUBSIDY_PAGE_SOURCE_BOTTOM_SHEET
        } else {
            SLASH_PRICE_SUBSIDY_PAGE_SOURCE_LIST_OF_PRODUCTS
        }.format(productId)
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_EDU_ARTICLE)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_EDU_ARTICLE)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressionOptOutMultipleProductSubsidyEvent(
        entrySource: String,
        isCtaEnabled: Boolean,
        ctaCopy: String,
        noOfProductShown: Int
    ) {
        val eventLabel =  listOf(
            entrySource,
            isCtaEnabled,
            ctaCopy,
            noOfProductShown.toString()
        ).joinToString(" - ")
        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_NON_EDITABLE_VARIANT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_NON_EDITABLE_VARIANT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressionOptOutSingleProductSubsidyEvent(
        entrySource: String,
        isCtaEnabled: Boolean,
        ctaCopy: String
    ) {
        val eventLabel = listOf(
            entrySource,
            isCtaEnabled,
            ctaCopy
        ).joinToString(" - ")
        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_NON_EDITABLE_PARENT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_NON_EDITABLE_PARENT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickCtaOptOutVariantEvent(
        entrySource: String,
        totalShownProduct: Int,
        totalSelectedProduct: Int,
        ctaCopy: String,
        listSelectedProductId: List<String>
    ) {
        val eventLabel = listOf(
            entrySource,
            totalShownProduct.toString(),
            totalSelectedProduct.toString(),
            ctaCopy,
        ).toMutableList().apply {
            if(listSelectedProductId.isNotEmpty()){
                add(listSelectedProductId.joinToString(","))
            }
        }.joinToString(" - ")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_CTA_OPT_OUT_VARIANT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_CTA_OPT_OUT_VARIANT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendDismissOptOutReasonBottomSheetEvent(
        entrySource: String,
        dismissSource: String,
        listSelectedProductId: List<String>
    ) {
        val eventLabel = listOf(
            entrySource,
            dismissSource,
        ).toMutableList().apply {
            if(listSelectedProductId.isNotEmpty()){
                add(listSelectedProductId.joinToString(","))
            }
        }.joinToString(" - ")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_CLOSE_BOTTOM_SHEET)
            .setEventCategory(SLASH_PRICE_SUBSIDY_OPT_OUT)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_CLOSE_BOTTOM_SHEET_OPT_OUT_REASON)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickButtonSubmitOptOutReasonEvent(
        entrySource: String,
        ctaCopy: String,
        listSelectedProductId: List<String>
    ) {
        val eventLabel = listOf(
            entrySource,
            ctaCopy,
        ).toMutableList().apply {
            if(listSelectedProductId.isNotEmpty()){
                add(listSelectedProductId.joinToString(","))
            }
        }.joinToString(" - ")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_CTA)
            .setEventCategory(SLASH_PRICE_SUBSIDY_OPT_OUT)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_SUBMIT_OPT_OUT_REASON)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickOptOutSingleProductSubsidyEvent(
        entrySource: String,
        ctaCopy: String,
        selectedProductId: String
    ) {
        val eventLabel = listOf(
            entrySource,
            ctaCopy,
            selectedProductId
        ).joinToString(" - ")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_CTA_OPT_OUT_PARENT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_CTA_OPT_OUT_PARENT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickOptOutSubsidyBulkEvent(
        totalSelectedProduct: Int,
        listProductId: List<String>
    ) {
        val eventLabel = listOf(
            totalSelectedProduct.toString(),
            listProductId.joinToString(",")
        ).joinToString(" - ")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_OPT_OUT_BULK)
            .setEventCategory(SLASH_PRICE_SUBSIDY_LIST_OF_PRODUCTS)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_OPT_OUT_BULK)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickOptOutSubsidyNonBulkEvent(productId: String) {
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_OPT_OUT_NON_BULK)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(productId)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_OPT_OUT_NON_BULK)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickOptOutSubsidyVariantEvent(productId: String) {
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_OPT_OUT_VARIANT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(productId)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_OPT_OUT_VARIANT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendImpressionNonSubsidyBottomSheetEvent(listProductId: List<String>) {
        val eventLabel = listProductId.joinToString(",")
        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_OPT_OUT_REASON_BOTTOM_SHEET_NO_SUBSIDY_PRODUCT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_OPT_OUT_REASON_BOTTOM_SHEET_NO_SUBSIDY_PRODUCT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickEduArticleOptOutReasonBottomSheetEvent(entrySource: String) {
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_EDU_ARTICLE)
            .setEventCategory(SLASH_PRICE_SUBSIDY_OPT_OUT)
            .setEventLabel(entrySource)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_EDU_ARTICLE_OPT_OUT_BOTTOM_SHEET)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendDismissOptOutMultipleProductBottomSheetEvent(
        entrySource: String,
        isCtaEnabled: Boolean,
        ctaCopy: String,
        dismissSource: String
    ) {
        val eventLabel = listOf(
            entrySource,
            isCtaEnabled.toString(),
            ctaCopy,
            dismissSource
        ).joinToString(" - ")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_CLOSE_BOTTOM_SHEET_VARIANT)
            .setEventCategory(SLASH_PRICE_SUBSIDY_BOTTOM_SHEET)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_CLOSE_BOTTOM_SHEET_VARIANT)
            .setBusinessUnit(CAMPAIGN_BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

}
