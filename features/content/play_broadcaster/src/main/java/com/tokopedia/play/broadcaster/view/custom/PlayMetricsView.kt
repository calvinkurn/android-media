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
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 10/06/20
 */
class PlayMetricsView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var prevView: View? = null

    init {
        gravity = Gravity.BOTTOM
        orientation = VERTICAL
    }

    fun show(metric: PlayMetricUiModel) {
        val transitionSet = TransitionSet()
        val previousView = prevView
        if (previousView != null) {
            transitionSet
                    .addTransition(Slide(Gravity.TOP)
                            .addTarget(previousView))
                    .addTransition(Fade(Fade.OUT)
                            .addTarget(previousView))
        }

        val newView: Typography = View.inflate(context, R.layout.item_play_metrics, null) as Typography
        newView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        newView.text = getSpannedMetric(metric)

        transitionSet
                .addTransition(Slide(Gravity.BOTTOM)
                        .addTarget(newView))
                .addTransition(Fade(Fade.IN)
                        .addTarget(newView))

        TransitionManager.beginDelayedTransition(this, transitionSet)

        removeView(previousView)
        addView(newView)
        prevView = newView
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
}