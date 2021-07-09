package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricType
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TrafficMetricViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TrafficMetricReportAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 18/06/20.
 */
class SummaryInfoViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.layout_summary_content) {

    val animationOffset = container.resources.getInteger(R.integer.play_summary_layout_animation_offset).toFloat()
    val animationDuration = container.resources.getInteger(R.integer.play_summary_layout_animation_duration_ms).toLong()

    private val flInfo = findViewById<FrameLayout>(R.id.fl_info)
    private val llMeta = findViewById<LinearLayout>(R.id.ll_meta)
    private val overflow = findViewById<View>(R.id.overflow)
    private val tvTitle = findViewById<Typography>(R.id.tv_title)
    private val ivCover = findViewById<ImageUnify>(R.id.iv_cover)
    private val tvDuration = findViewById<Typography>(R.id.tv_duration)
    private val recyclerView = findViewById<RecyclerView>(R.id.rv_info)
    private val layoutError = findViewById<ConstraintLayout>(R.id.layout_summary_error)
    private val tvErrorTryAgain = findViewById<Typography>(R.id.tv_error_try_again)

    private val trafficMetricReportAdapter = TrafficMetricReportAdapter(object : TrafficMetricViewHolder.Listener {
        override fun onLabelClicked(metricType: TrafficMetricType) {
            listener.onMetricClicked(this@SummaryInfoViewComponent, metricType)
        }
    })

    init {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(container.context, RecyclerView.VERTICAL, false)
            adapter = trafficMetricReportAdapter
        }
    }

    fun entranceAnimation(container: ViewGroup) {
        container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)

                flInfo.translationY = animationOffset
                overflow.translationY = animationOffset
                llMeta.translationY = -animationOffset
                flInfo.animate().translationYBy(-animationOffset).duration = animationDuration
                llMeta.animate().translationYBy(animationOffset).duration = animationDuration
                overflow.animate().translationYBy(-animationOffset).duration = animationDuration
            }
        })
    }

    fun setChannelTitle(title: String) {
        tvTitle.text = title
    }

    fun setChannelCover(coverUrl: String) {
        ivCover.setImageUrl(coverUrl)
    }

    fun setLiveDuration(data: LiveDurationUiModel) {
        tvDuration.text = data.duration
    }

    fun addTrafficMetric(metric: TrafficMetricUiModel, position: Int) {
        trafficMetricReportAdapter.addItem(position, metric)
    }

    fun addTrafficMetrics(dataList: List<TrafficMetricUiModel>) {
        trafficMetricReportAdapter.addItems(dataList)
    }

    fun showError(onRetry: () -> Unit) {
        layoutError.show()
        tvErrorTryAgain.setOnClickListener { onRetry() }
    }

    fun hideError() {
        layoutError.hide()
    }

    interface Listener {
        fun onMetricClicked(view: SummaryInfoViewComponent, metricType: TrafficMetricType)
    }
}