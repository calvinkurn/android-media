package com.tokopedia.product.detail.postatc.component.recommendation

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationUiModel(
    var widget: RecommendationWidget? = null,
    override val name: String,
    override val type: String,
    override val impressHolder: ImpressHolder = ImpressHolder()
) : PostAtcUiModel {
    override val id = hashCode()
}
