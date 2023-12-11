package com.tokopedia.recommendation_widget_common.widget.loading

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.PartialRecomShimmeringGridListHorizontalBinding
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetStealTheLookShimmerBinding
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView

internal class StealTheLookShimmeringView:
    ConstraintLayout,
    IRecommendationWidgetView<StealTheLookShimmeringModel> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        RecommendationWidgetStealTheLookShimmerBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    override val layoutId: Int
        get() = LAYOUT

    override fun bind(model: StealTheLookShimmeringModel) { }

    override fun recycle() {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.recommendation_widget_steal_the_look_shimmer
    }
}
