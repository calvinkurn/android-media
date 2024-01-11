package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartGwpGiftUiModel(
    val offerId: Long,
    val tierId: Long,
    val ribbonText: String,
    val ctaText: String,
    val giftList: List<ProductGiftUiModel>
) : Visitable<MiniCartListAdapterTypeFactory> {
    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int = typeFactory.type(this)
}

