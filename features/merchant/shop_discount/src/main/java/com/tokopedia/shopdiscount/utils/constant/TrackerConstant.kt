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
    }

    object EventCategory{
        const val SLASH_PRICE_SUBSIDY_LIST_OF_PRODUCTS = "slash price page - list of products"
        const val SLASH_PRICE_SUBSIDY_BOTTOM_SHEET = "slash price page - bottomsheet"
    }

    object EventLabel {
        const val SLASH_PRICE_SUBSIDY_NON_VARIANT_PRODUCT = "product parent - %1s"
        const val SLASH_PRICE_SUBSIDY_VARIANT_PRODUCT = "product variant - %1s"
        const val SLASH_PRICE_SUBSIDY_DETAIL_LIST_OF_PRODUCTS = "page source list page - %1s"
        const val SLASH_PRICE_SUBSIDY_DETAIL_BOTTOM_SHEET = "page source bottomsheet - %1s"

    }

    object TrackerId{
        const val TRACKER_ID_IMPRESSION_COACH_MARK_PRODUCT_LIST = "49490"
        const val TRACKER_ID_IMPRESSION_COACH_MARK_BOTTOM_SHEET = "49491"
        const val TRACKER_ID_CLICK_SUBSIDY_INFORMATION_PRODUCT_LIST = "49494"
        const val TRACKER_ID_CLICK_SUBSIDY_INFORMATION_BOTTOM_SHEET = "49614"
        const val TRACKER_ID_IMPRESSION_SUBSIDY_DETAIL = "49495"
        const val TRACKER_ID_CLICK_EDU_ARTICLE = "49496"
    }
}
