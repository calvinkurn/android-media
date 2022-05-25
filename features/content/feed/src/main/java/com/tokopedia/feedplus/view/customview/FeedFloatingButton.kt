package com.tokopedia.feedplus.view.customview

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.feedplus.R

/**
 * Created By : Jonathan Darwin on May 25, 2022
 */
class FeedFloatingButton : LinearLayout, View.OnClickListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    /** View */
    private val flTextWrapper: FrameLayout
    private val overlayView: View = View(context)

    init {
        val view = View.inflate(context, R.layout.view_feed_floating_button, this)
        super.setOnClickListener(this)

        flTextWrapper = view.findViewById(R.id.fl_fab_text_wrapper)
    }

    private var mOnClickListener: OnClickListener? = null
    private var isExpand: Boolean = false

    override fun onClick(p0: View?) {
        shrink()
        /** TODO: change icon to X */
        mOnClickListener?.onClick(p0)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

    fun expand() {
        if(!isExpand) {
            flTextWrapper.measure(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            val targetWidth = flTextWrapper.measuredWidth

            ValueAnimator.ofInt(0, targetWidth).apply {
                duration = ANIMATION_DURATION
                addUpdateListener {
                    flTextWrapper.layoutParams = flTextWrapper.layoutParams.apply {
                        width = it.animatedValue as Int
                    }
                }
            }.start()

            isExpand = true
        }
    }

    fun shrink() {
        if(isExpand) {
            ValueAnimator.ofInt(flTextWrapper.measuredWidth, 0).apply {
                duration = ANIMATION_DURATION
                addUpdateListener {
                    flTextWrapper.layoutParams = flTextWrapper.layoutParams.apply {
                        width = it.animatedValue as Int
                    }
                }
            }.start()

            isExpand = false
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 200L
    }
}