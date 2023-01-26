package com.tokopedia.product.detail.postatc.component.recommendation

import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationUiModel(
    var widget: RecommendationWidget? = null,
    override val name: String
) : PostAtcUiModel
