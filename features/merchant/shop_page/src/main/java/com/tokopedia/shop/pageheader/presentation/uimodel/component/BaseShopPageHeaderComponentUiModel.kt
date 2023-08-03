package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

interface BaseShopPageHeaderComponentUiModel : Visitable<BaseAdapterTypeFactory> {

    object ComponentType {
        const val IMAGE_ONLY = "image_only"
        const val BADGE_TEXT_VALUE = "badge_text_value"
        const val BUTTON = "button"
        const val IMAGE_TEXT = "image_text"
    }

    object ComponentName {
        const val SHOP_LOGO = "shop_logo"
        const val SHOP_NAME = "shop_name"
        const val SHOP_RATING = "shop_rating"
        const val SHOP_OPERATIONAL_HOUR = "shop_operational_hour"
        const val BUTTON_SHOP_NOTES = "shop_notes"
        const val BUTTON_CHAT = "chat"
        const val BUTTON_FOLLOW = "follow_member"
        const val BUTTON_PLAY = "play"
    }

    val name: String
    val type: String
    var componentPosition: Int
}
