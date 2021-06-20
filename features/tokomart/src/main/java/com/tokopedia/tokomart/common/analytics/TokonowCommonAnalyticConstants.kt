package com.tokopedia.tokomart.common.analytics

object TokonowCommonAnalyticConstants {

    object EVENT{
        const val EVENT_CLICK_TOKONOW = "clickTokoNow"
        const val EVENT_SELECT_CONTENT = "select_content"
        const val EVENT_VIEW_ITEM = "view_item"
    }

    object CATEGORY{
        const val EVENT_CATEGORY_TOP_NAV = "tokonow - top nav"
    }

    object KEY {
        const val KEY_AFFINITY_LABEL = "affinityLabel"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_CREATIVE_NAME = "creative_name"
        const val KEY_CREATIVE_SLOT = "creative_slot"
        const val KEY_DIMENSION_104 = "dimension104"
        const val KEY_DIMENSION_38 = "dimension38"
        const val KEY_DIMENSION_79 = "dimension79"
        const val KEY_DIMENSION_82 = "dimension82"
        const val KEY_DIMENSION_49 = "dimension49"
        const val KEY_USER_ID = "userId"
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
    }

    object VALUE {
        const val CURRENT_SITE_TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT_PHYSICAL_GOODS = "Physical Goods"
    }
}