package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoByShopIdUiModel

data class TncUiModel(
    val tnc: String
) : ImpressHolder()

data class WidgetItem(
    val item: List<OfferingInfoByShopIdUiModel>
) : Visitable<OlpAdapterTypeFactory> {
    override fun type(typeFactory: OlpAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
