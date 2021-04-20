package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.view.adapter.TrafficMetricReportAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 18/06/20.
 */
class SummaryInfoViewComponent(
        container: ViewGroup
) : ViewComponent(container, R.id.layout_summary_content) {

    val animationOffset = container.resources.getInteger(R.integer.play_summary_layout_animation_offset).toFloat()
    val animationDuration = container.resources.getInteger(R.integer.play_summary_layout_animation_duration_ms).toLong()

    private val flInfo = findViewById<FrameLayout>(R.id.fl_info)
    private val llMeta = findViewById<ConstraintLayout>(R.id.ll_meta)
    private val overflow = findViewById<View>(R.id.overflow)
    private val tvTitle = findViewById<Typography>(R.id.tv_title)
    private val ivCover = findViewById<AppCompatImageView>(R.id.iv_cover)
    private val tvDuration = findViewById<Typography>(R.id.tv_duration)
    private val ticker = findViewById<Ticker>(R.id.ticker_info)
    private val recyclerView = findViewById<RecyclerView>(R.id.rv_info)
    private val layoutError = findViewById<ConstraintLayout>(R.id.layout_summary_error)
    private val tvErrorTryAgain = findViewById<Typography>(R.id.tv_error_try_again)

    private val trafficMetricReportAdapter = TrafficMetricReportAdapter()

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
        ivCover.loadImageRounded(coverUrl)
    }

    fun setLiveDuration(data: LiveDurationUiModel) {
        tvDuration.text = data.duration
    }

    fun setSummaryInfo(dataList: List<TrafficMetricUiModel>) {
        trafficMetricReportAdapter.setItems(dataList)
        trafficMetricReportAdapter.notifyDataSetChanged()
    }

    fun showTicker(title: String, description: String) {
        ticker.show()
        ticker.tickerTitle = title
        ticker.setHtmlDescription(description)
    }

    fun showError(onRetry: () -> Unit) {
        layoutError.show()
        tvErrorTryAgain.setOnClickListener { onRetry() }
    }

    fun hideError() {
        layoutError.hide()
    }
}