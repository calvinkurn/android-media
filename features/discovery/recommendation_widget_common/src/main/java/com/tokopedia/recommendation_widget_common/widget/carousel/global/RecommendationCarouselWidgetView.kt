package com.tokopedia.recommendation_widget_common.widget.carousel.global

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.recommendation_widget_common.databinding.LayoutWidgetRecommendationCarouselBinding
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView

/**
 * Created by frenzel on 27/03/23
 */
class RecommendationCarouselWidgetView : FrameLayout, IRecommendationWidgetView<RecommendationCarouselModel> {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        val LAYOUT = com.tokopedia.recommendation_widget_common.R.layout.layout_widget_recommendation_carousel
    }

    private var binding: LayoutWidgetRecommendationCarouselBinding? = null

    init {
        binding = LayoutWidgetRecommendationCarouselBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun bind(model: RecommendationCarouselModel) {
    }
}
