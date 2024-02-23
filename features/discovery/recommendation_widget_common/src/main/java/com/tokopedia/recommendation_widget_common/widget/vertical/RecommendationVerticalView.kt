package com.tokopedia.recommendation_widget_common.widget.vertical

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.addVerticalTrackListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetVerticalLayoutBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalMapper.mapVisitableList
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

class RecommendationVerticalView :
    ConstraintLayout,
    IRecommendationWidgetView<RecommendationVerticalModel>,
    RecommendationHeaderListener,
    DefaultLifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val trackingQueue: TrackingQueue = TrackingQueue(context)
    private val binding by lazyThreadSafetyNone {
        RecommendationWidgetVerticalLayoutBinding.inflate(LayoutInflater.from(context), this)
    }
    private val recomAdapter by lazyThreadSafetyNone {
        RecommendationVerticalAdapter(RecommendationVerticalTypeFactoryImpl(trackingQueue))
    }

    private var hasRecomScrollListener: Boolean = false

    override val layoutId: Int
        get() = LAYOUT

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        trackingQueue.sendAll()
    }

    override fun bind(model: RecommendationVerticalModel) {
        setupHeader(model.widget)
        setupProductCards(model)
    }

    override fun recycle() {}

    private fun setupHeader(widget: RecommendationWidget) {
        binding.headerViewRecommendationVertical.bindData(widget, this)
        binding.headerViewRecommendationVertical.seeAllButton?.hide()
    }

    private fun setupProductCards(model: RecommendationVerticalModel) {
        if (binding.rvRecommendationVertical.adapter != recomAdapter) {
            binding.rvRecommendationVertical.adapter = recomAdapter
        }
        trackEnterPage()
        recomAdapter.submitList(mapVisitableList(model = model))
    }

    private fun trackEnterPage() {
        if(hasRecomScrollListener) return
        binding.rvRecommendationVertical.addVerticalTrackListener(
            recommendationTriggerObject = RecommendationTriggerObject(
                viewHolders = listOf(
                    RecommendationVerticalProductCardViewHolder::class.java,
                    RecommendationVerticalSeeMoreViewHolder::class.java,
                )
            )
        )
        hasRecomScrollListener = true
    }

    override fun onSeeAllClick(link: String) {}

    override fun onChannelExpired(widget: RecommendationWidget) {}

    companion object {
        val LAYOUT = recommendation_widget_commonR.layout.recommendation_widget_vertical_layout
    }
}
