package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartGwpGiftUiModel(
    val offerId: Long,
    val offerTypeId: Long,
    val tierId: Long,
    val ribbonText: String,
    val ctaText: String,
    val giftList: List<ProductGiftUiModel>,
    val progressiveInfoText: String,
    val position: Int,
    val warehouseId: Long,
    val shopId: String
) : Visitable<MiniCartListAdapterTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int = typeFactory.type(this)
}

