package com.tokopedia.shopdiscount.utils.constant

object TrackerConstant {
    const val PHYSICAL_GOODS_BUSINESS_UNIT = "physical goods"
    const val CAMPAIGN_BUSINESS_UNIT = "campaign"
    const val SLASH_PRICE_SET_DISCOUNT = "slash price page - set discount - %1s"
    const val SLASH_PRICE_LIST_OF_PRODUCTS = "slash price page - list of products"
    const val TOKOPEDIA_SELLER = "tokopediaseller"
    const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    const val CLICK_PG = "clickPG"
    const val CLICK_SAVE = "click save"
    const val CLICK_ADD_PRODUCT = "click add product"
    const val CREATE = "create"
    const val EDIT = "edit"
    const val VIEW_PG_IRIS = "viewPGIris"

    object Key{
        const val TRACKER_ID = "trackerId"
    }
    object EventAction {
        const val IMPRESSION_COACH_MARK = "impression coachmark"
        const val CLICK_SUBSIDY_INFORMATION = "click subsidy information"
        const val IMPRESSION_SUBSIDY_DETAIL = "impression subsidy detail"
        const val CLICK_EDU_ARTICLE = "click edu article"
        const val IMPRESSION_NON_EDITABLE_VARIANT = "impression non editable variant"
        const val IMPRESSION_NON_EDITABLE_PARENT = "impression non editable parent"
        const val CLICK_CTA_OPT_OUT_VARIANT = "click cta keluar variant"
        const val CLICK_CLOSE_BOTTOM_SHEET = "click close bottomsheet"
        const val CLICK_CTA = "click cta"
        const val CLICK_CTA_OPT_OUT_PARENT = "click cta keluar parent"
        const val CLICK_OPT_OUT_BULK = "click opt out bulk"
        const val CLICK_OPT_OUT_NON_BULK = "click opt out"
        const val CLICK_OPT_OUT_VARIANT = "click opt out variant"
        const val IMPRESSION_OPT_OUT_REASON_BOTTOM_SHEET_NO_SUBSIDY_PRODUCT = "impression non subsidized"
    }

    object EventCategory{
        const val SLASH_PRICE_SUBSIDY_LIST_OF_PRODUCTS = "slash price page - list of products"
        const val SLASH_PRICE_SUBSIDY_BOTTOM_SHEET = "slash price page - bottomsheet"
        const val SLASH_PRICE_SUBSIDY_OPT_OUT = "slash price page - opt out"
    }

    object EventLabel {
        const val SLASH_PRICE_SUBSIDY_NON_VARIANT_PRODUCT = "product parent - %1s"
        const val SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT = "product variant - %1s"
        const val SLASH_PRICE_SUBSIDY_PAGE_SOURCE_LIST_OF_PRODUCTS = "list page - %1s"
        const val SLASH_PRICE_SUBSIDY_PAGE_SOURCE_BOTTOM_SHEET = "bottomsheet - %1s"
    }

    object TrackerId{
        const val TRACKER_ID_IMPRESSION_COACH_MARK_PRODUCT_LIST = "49490"
        const val TRACKER_ID_IMPRESSION_COACH_MARK_BOTTOM_SHEET = "49491"
        const val TRACKER_ID_CLICK_SUBSIDY_INFORMATION_PRODUCT_LIST = "49494"
        const val TRACKER_ID_CLICK_SUBSIDY_INFORMATION_BOTTOM_SHEET = "49614"
        const val TRACKER_ID_IMPRESSION_SUBSIDY_DETAIL = "49495"
        const val TRACKER_ID_CLICK_EDU_ARTICLE = "49496"
        const val TRACKER_ID_IMPRESSION_NON_EDITABLE_VARIANT = "49497"
        const val TRACKER_ID_IMPRESSION_NON_EDITABLE_PARENT = "49498"
        const val TRACKER_ID_CLICK_CTA_OPT_OUT_VARIANT = "49499"
        const val TRACKER_ID_CLICK_CLOSE_BOTTOM_SHEET_OPT_OUT_REASON = "49500"
        const val TRACKER_ID_CLICK_SUBMIT_OPT_OUT_REASON = "49501"
        const val TRACKER_ID_CLICK_CTA_OPT_OUT_PARENT = "49506"
        const val TRACKER_ID_CLICK_OPT_OUT_BULK = "49507"
        const val TRACKER_ID_CLICK_OPT_OUT_NON_BULK = "49508"
        const val TRACKER_ID_CLICK_OPT_OUT_VARIANT = "49509"
        const val TRACKER_ID_IMPRESSION_OPT_OUT_REASON_BOTTOM_SHEET_NO_SUBSIDY_PRODUCT = "49510"
        const val TRACKER_ID_CLICK_EDU_ARTICLE_OPT_OUT_BOTTOM_SHEET = "49526"
    }
}
