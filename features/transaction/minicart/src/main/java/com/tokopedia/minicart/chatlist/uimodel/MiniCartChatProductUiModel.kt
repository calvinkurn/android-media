package com.tokopedia.minicart.chatlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory

data class MiniCartChatProductUiModel(
    var productId: String = "",
    var productImageUrl: String = "",
    var productName: String = "",
    var productSlashPriceLabel: String = "",
    var productOriginalPrice: Double = 0.0,
    var productPrice: Double = 0.0,
    var isChecked: Boolean = false,
    var isProductDisabled: Boolean = false,
    var size: Int = 0,
    var productInformation: List<String> = emptyList()
) : Visitable<MiniCartChatListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartChatListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
