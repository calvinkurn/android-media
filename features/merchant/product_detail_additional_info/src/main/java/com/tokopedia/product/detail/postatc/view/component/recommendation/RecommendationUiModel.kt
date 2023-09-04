package com.tokopedia.product.detail.postatc.view.component.recommendation

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationUiModel(
    override val name: String,
    override val type: String,
    override val impressHolder: ImpressHolder = ImpressHolder(),
    val productId: String,
    var widget: RecommendationWidget? = null,
    val queryParam: String = "",
    val warehouseId: String
) : PostAtcUiModel {
    override val id = hashCode()
    override fun equalsWith(newItem: PostAtcUiModel): Boolean {
        return newItem is RecommendationUiModel &&
            widget == newItem.widget
    }
    override fun newInstance(): PostAtcUiModel = copy()
}
