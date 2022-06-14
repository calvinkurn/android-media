package com.tokopedia.oldminicart.chatlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldminicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory

data class MiniCartChatProductUiModel(
    var productId: String = "",
    var productImageUrl: String = "",
    var productName: String = "",
    var productSlashPriceLabel: String = "",
    var productOriginalPrice: Long = 0L,
    var productPrice: Long = 0L,
    var isChecked: Boolean = false,
    var isProductDisabled: Boolean = false,
    var size: Int = 0,
    var productInformation: List<String> = emptyList(),
) : Visitable<MiniCartChatListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartChatListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}