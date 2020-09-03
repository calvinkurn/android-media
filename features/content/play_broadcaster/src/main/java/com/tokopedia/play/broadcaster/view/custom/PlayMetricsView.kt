package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

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
    private val textViewList: List<TextView>

    private val metricsChannel = BroadcastChannel<PlayMetricUiModel>(Channel.BUFFERED)

    init {
        scope.launch(Dispatchers.Main.immediate) { initMetricsChannel() }
        gravity = Gravity.BOTTOM
        orientation = VERTICAL
        textViewList = List(2) { getTextViewInstance() }
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
        val nextIndex = (currentIndex + 1) % textViewList.size
        val nextView = textViewList[nextIndex]

        nextView.text = getSpannedMetric(metric)

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

    private fun getSpannedMetric(metric: PlayMetricUiModel): CharSequence {
        val spannedText = SpannableString(metric.fullSentence)
        val secondSentenceFirstIndex = spannedText.indexOf(metric.secondSentence)
        val secondSentenceLastIndex = secondSentenceFirstIndex + metric.secondSentence.length
        spannedText.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.dark_G500)
                ), secondSentenceFirstIndex, secondSentenceLastIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannedText.setSpan(StyleSpan(Typeface.BOLD), secondSentenceFirstIndex, secondSentenceLastIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        return spannedText
    }

    private fun getTextViewInstance(): TextView {
        val view = View.inflate(context, R.layout.item_play_metrics, null) as TextView
        view.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return view
    }

    private suspend fun initMetricsChannel() = withContext(Dispatchers.Default) {
        metricsChannel.asFlow().collect {
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
        removeMetric(textViewList[currentIndex])
    }

    companion object {
        private const val IN_BETWEEN_DELAY = 100L
    }
}