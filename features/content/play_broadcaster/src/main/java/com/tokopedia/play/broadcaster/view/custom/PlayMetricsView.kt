package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

/**
 * Created by jegul on 10/06/20
 */
class PlayMetricsView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var currentIndex = -1
    private val metricBubbleList: List<PlayMetricBubble>

    private val metricsChannel = Channel<PlayMetricUiModel>(Channel.BUFFERED)

    init {
        scope.launch(Dispatchers.Main.immediate) { initMetricsChannel() }
        gravity = Gravity.BOTTOM
        orientation = VERTICAL
        metricBubbleList = List(2) { getMetricBubbleInstance() }
    }

    fun addMetricToQueue(metric: PlayMetricUiModel) {
        addMetricsToQueue(listOf(metric))
    }

    fun addMetricsToQueue(metrics: List<PlayMetricUiModel>) {
        scope.launch {
            metrics.forEach { metricsChannel.send(it) }
        }
    }

    private fun show(metric: PlayMetricUiModel) {
        val nextIndex = (currentIndex + 1) % metricBubbleList.size
        val nextView = metricBubbleList[nextIndex]

        nextView.setMetric(metric)

        val transition = TransitionSet()
                .addTransition(Slide(Gravity.BOTTOM)
                        .addTarget(nextView))
                .addTransition(Fade(Fade.IN)
                        .addTarget(nextView))

        TransitionManager.beginDelayedTransition(this, transition)
        addView(nextView)

        currentIndex = nextIndex
    }

    private fun removeMetric(view: View) {
        val transition = TransitionSet()
                .addTransition(Slide(Gravity.TOP)
                        .addTarget(view))
                .addTransition(Fade(Fade.OUT)
                        .addTarget(view))

        TransitionManager.beginDelayedTransition(this, transition)
        removeView(view)

        println("MetricView Removed: $view")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancelChildren()
    }

    private fun getMetricBubbleInstance(): PlayMetricBubble {
        return PlayMetricBubble(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private suspend fun initMetricsChannel() = withContext(Dispatchers.Default) {
        metricsChannel.consumeAsFlow().collect {
            onRetrievedNewMetric(it)
            delay(it.interval)
            removeCurrentMetric()
            delay(IN_BETWEEN_DELAY)
        }
    }

    private suspend fun onRetrievedNewMetric(newMetric: PlayMetricUiModel) = withContext(Dispatchers.Main) {
        show(newMetric)
    }

    private suspend fun removeCurrentMetric() = withContext(Dispatchers.Main) {
        removeMetric(metricBubbleList[currentIndex])
    }

    companion object {
        private const val IN_BETWEEN_DELAY = 100L
    }
}