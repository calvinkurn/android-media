package com.tokopedia.product.detail.postatc.view.component.fallback

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

data class FallbackUiModel(
    override val name: String = "",
    override val type: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder(),
    val cartId: String
) : PostAtcUiModel {
    override val id: Int = hashCode()
    override fun equalsWith(newItem: PostAtcUiModel): Boolean = this.id == newItem.id
    override fun newInstance(): PostAtcUiModel = copy()
}
