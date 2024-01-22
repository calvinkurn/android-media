package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.helper.StartPagerSnapHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetStealTheLookBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.NpaLinearLayoutManager
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

class StealTheLookWidgetView :
    ConstraintLayout,
    IRecommendationWidgetView<StealTheLookWidgetModel>,
    RecommendationHeaderListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding by lazyThreadSafetyNone {
        RecommendationWidgetStealTheLookBinding.inflate(LayoutInflater.from(context), this)
    }

    private val stealTheLookAdapter by lazyThreadSafetyNone {
        StealTheLookPagingAdapter()
    }

    private val layoutManager by lazy { NpaLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL) }

    private val snapHelper: StartPagerSnapHelper by lazy {
        StartPagerSnapHelper().also { it.itemPerPage = 1 }
    }

    override val layoutId: Int
        get() = LAYOUT

    override fun bind(model: StealTheLookWidgetModel) {
        setupHeader(model.widget)
        setupRecyclerView(model)
        setupPageControl(model)
    }

    override fun recycle() { }

    private fun setupHeader(widget: RecommendationWidget) {
        binding.headerViewRecommendationVertical.bindData(widget, this)
        binding.headerViewRecommendationVertical.seeAllButton?.hide()
    }

    private fun setupRecyclerView(model: StealTheLookWidgetModel) {
        with(binding.rvStealTheLook) {
            val lParams = layoutParams
            lParams.width = StealTheLookViewUtil.getContainerWidth(context)
            layoutParams = lParams

            if(layoutManager != this@StealTheLookWidgetView.layoutManager) {
                layoutManager = this@StealTheLookWidgetView.layoutManager
            }
            snapHelper.attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val currentPagePosition = this@StealTheLookWidgetView.layoutManager.findFirstCompletelyVisibleItemPosition()
                        changePage(currentPagePosition)
                    }
                }
            })
            if (this.adapter != stealTheLookAdapter) {
                this.adapter = stealTheLookAdapter
            }
            stealTheLookAdapter.submitList(model.itemList)
        }
    }

    private fun setupPageControl(model: StealTheLookWidgetModel) {
        binding.stlPageControl.setIndicator(model.itemList.count())
    }

    private fun changePage(position: Int) {
        binding.stlPageControl.setCurrentIndicator(position)
    }

    override fun onSeeAllClick(link: String) {}

    override fun onChannelExpired(widget: RecommendationWidget) {}

    companion object {
        val LAYOUT = recommendation_widget_commonR.layout.recommendation_widget_steal_the_look
    }
}
