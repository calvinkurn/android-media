package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by frenzel on 11/03/23
 */
interface RecomVisitable: Visitable<RecomTypeFactory> {
    val type: String
    val name: String
    var recomWidgetData: RecommendationWidget?
    val state: Int
    val verticalPosition: Int
    var isInitialized: Boolean
}
