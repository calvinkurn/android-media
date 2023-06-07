package com.tokopedia.product.detail.postatc.view.component.addons

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

data class AddonsUiModel(
    override val name: String,
    override val type: String,
    override val impressHolder: ImpressHolder = ImpressHolder(),
    val data: Data = Data()
) : PostAtcUiModel {

    override val id: Int = hashCode()

    override fun equalsWith(newItem: PostAtcUiModel) =
        newItem is AddonsUiModel

    override fun newInstance(): PostAtcUiModel = this.copy()

    data class Data(
        val title: String = "",
        val productId: String = "",
        val warehouseId: String = "",
        val isTokoCabang: Boolean = false,
        val selectedAddonsIds: List<String> = emptyList()
    )
}
