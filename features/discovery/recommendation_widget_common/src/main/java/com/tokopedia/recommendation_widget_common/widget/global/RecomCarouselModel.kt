package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselChipListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener

/**
 * Created by frenzel on 27/03/23
 */
data class RecomCarouselModel(
    override val type: String = "",
    override val name: String = "",
    override var recomWidgetData: RecommendationWidget? = null,
    override val state: Int = 0,
    override val verticalPosition: Int = -1,
    override var isInitialized: Boolean = false,
    val filterData: List<AnnotationChip> = listOf(),
    val basicListener: RecomCarouselWidgetBasicListener?,
    val tokonowListener: RecommendationCarouselTokonowListener?,
    val chipListener: RecomCarouselChipListener? = null,
    val isTokonow: Boolean = false
) : RecomVisitable {
    override fun type(typeFactory: RecomTypeFactory): Int {
        return typeFactory.type(this)
    }
}
