package com.tokopedia.shop.pageheader.presentation.uimodel.component

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

interface BaseShopHeaderComponentUiModel : Visitable<BaseAdapterTypeFactory> {

    object ComponentType {
        const val IMAGE_ONLY = "image_only"
        const val BADGE_TEXT_VALUE = "badge_text_value"
        const val BUTTON = "button"
    }
    object ComponentName{
        const val SHOP_LOGO = "shop_logo"
        const val SHOP_NAME = "shop_name"
        const val BUTTON_SHOP_NOTES = "catatan_toko"
        const val BUTTON_CHAT = "chat"
        const val BUTTON_FOLLOW = "follow_member"
    }

    val name: String
    val type: String
}