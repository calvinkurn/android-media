package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.transition.*
import com.tokopedia.play.broadcaster.R
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
    }

    fun show(text: CharSequence) {
        val transitionSet = TransitionSet()
        val previousView = prevView
        if (previousView != null) {
            transitionSet
                    .addTransition(
                            Slide(Gravity.TOP)
                                    .addTarget(previousView)
                    )
                    .addTransition(
                            Fade(Fade.OUT)
                                    .addTarget(previousView)
                    )
        }

        val newView: Typography = View.inflate(context, R.layout.item_play_metrics, null) as Typography
        newView.text = text

        transitionSet
                .addTransition(
                        Slide(Gravity.BOTTOM)
                                .addTarget(newView)
                )
                .addTransition(
                        Fade(Fade.IN)
                                .addTarget(newView)
                )

        transitionSet.addListener(object: TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                prevView = newView
            }
        })

        TransitionManager.beginDelayedTransition(this, transitionSet)

        removeView(previousView)
        addView(newView)
    }
}