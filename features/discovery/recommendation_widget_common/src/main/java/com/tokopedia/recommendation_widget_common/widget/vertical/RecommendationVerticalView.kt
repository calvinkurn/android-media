package com.tokopedia.recommendation_widget_common.widget.vertical

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetVerticalLayoutBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalMapper.mapVisitableList
import com.tokopedia.recommendation_widget_common.widget.vertical.model.RecommendationVerticalModel

class RecommendationVerticalView :
    ConstraintLayout,
    IRecommendationWidgetView<RecommendationVerticalModel>,
    RecommendationHeaderListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding by lazyThreadSafetyNone {
        RecommendationWidgetVerticalLayoutBinding.inflate(LayoutInflater.from(context), this)
    }
    private val recomAdapter by lazyThreadSafetyNone {
        RecommendationVerticalAdapter(RecommendationVerticalTypeFactoryImpl())
    }

    override val layoutId: Int
        get() = LAYOUT

    override fun bind(model: RecommendationVerticalModel) {
        setupHeader(model.widget)
        setupProductCards(model.widget)
    }

    private fun setupHeader(widget: RecommendationWidget) {
        binding.headerViewRecommendationVertical.bindData(widget, this)
        binding.headerViewRecommendationVertical.seeAllButton?.hide()
    }

    private fun setupProductCards(widget: RecommendationWidget) {
        if (binding.rvRecommendationVertical.adapter != recomAdapter) {
            binding.rvRecommendationVertical.adapter = recomAdapter
        }
        recomAdapter.submitList(mapVisitableList(widget = widget))
    }

    override fun onSeeAllClick(link: String) {}

    override fun onChannelExpired(widget: RecommendationWidget) {}

    companion object {
        val LAYOUT = com.tokopedia.recommendation_widget_common.R.layout.recommendation_widget_vertical_layout
    }
}
