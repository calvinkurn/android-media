package com.tokopedia.product.detail.postatc.view.component.loading

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

data class LoadingUiModel(
    override val name: String = "",
    override val type: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder()
) : PostAtcUiModel {
    override val id = hashCode()
    override fun equalsWith(newItem: PostAtcUiModel) = id == newItem.id
    override fun newInstance(): PostAtcUiModel = copy()
}
