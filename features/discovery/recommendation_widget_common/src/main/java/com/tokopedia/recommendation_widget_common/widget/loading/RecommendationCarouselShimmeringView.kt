package com.tokopedia.recommendation_widget_common.widget.loading

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.PartialRecomShimmeringGridListHorizontalBinding
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView

internal class RecommendationCarouselShimmeringView:
    ConstraintLayout,
    IRecommendationWidgetView<RecommendationCarouselShimmeringModel> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        PartialRecomShimmeringGridListHorizontalBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    override val layoutId: Int
        get() = LAYOUT

    override fun bind(model: RecommendationCarouselShimmeringModel) { }

    override fun recycle() {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.partial_recom_shimmering_grid_list_horizontal
    }
}
