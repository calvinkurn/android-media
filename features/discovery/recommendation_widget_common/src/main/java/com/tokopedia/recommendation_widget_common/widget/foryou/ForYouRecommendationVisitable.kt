package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface ForYouRecommendationVisitable : Visitable<ForYouRecommendationTypeFactory> {

    fun areItemsTheSame(other: Any): Boolean

    fun areContentsTheSame(other: Any): Boolean
}
