package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetStealTheLookBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

class StealTheLookWidgetView :
    ConstraintLayout,
    IRecommendationWidgetView<StealTheLookWidgetModel>,
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
        RecommendationWidgetStealTheLookBinding.inflate(LayoutInflater.from(context), this)
    }
    private val adapter by lazyThreadSafetyNone {
        StealTheLookPagingAdapter(trackingQueue)
    }

    override val layoutId: Int
        get() = LAYOUT

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        trackingQueue.sendAll()
    }

    override fun bind(model: StealTheLookWidgetModel) {
        setupHeader(model.widget)
        setupPages(model)
    }

    override fun recycle() { }

    private fun setupHeader(widget: RecommendationWidget) {
        binding.headerViewRecommendationVertical.bindData(widget, this)
        binding.headerViewRecommendationVertical.seeAllButton?.hide()
    }

    private fun setupPages(model: StealTheLookWidgetModel) {
        if (binding.rvStealTheLook.adapter != adapter) {
            binding.rvStealTheLook.adapter = adapter
        }
        adapter.submitList(model.itemList)
    }

    override fun onSeeAllClick(link: String) {}

    override fun onChannelExpired(widget: RecommendationWidget) {}

    companion object {
        val LAYOUT = recommendation_widget_commonR.layout.recommendation_widget_steal_the_look
    }
}
