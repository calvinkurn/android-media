package com.tokopedia.feedplus.view.customview

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.feedplus.R
import com.tokopedia.iconunify.IconUnify
import kotlinx.coroutines.*
import kotlin.concurrent.timer

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
    private val icFab: IconUnify

    init {
        val view = View.inflate(context, R.layout.view_feed_floating_button, this)
        super.setOnClickListener(this)

        flTextWrapper = view.findViewById(R.id.fl_fab_text_wrapper)
        icFab = view.findViewById(R.id.ic_fab)
    }

    private var mOnClickListener: OnClickListener? = null
    private var isExpand: Boolean = false
    private var isMenuOpen: Boolean = false

    private val dispatchers = CoroutineDispatchersProvider
    private var job: Job? = null
    private val scope = CoroutineScope(dispatchers.computation)

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

    fun checkFabMenuStatusWithTimer(isMenuOpenCallback: () -> Boolean) {
        stopTimer()
        job = scope.launch {
            while (isActive) {
                delay(TIMER_CHECK_MENU_STATUS_DURATION)

                withContext(dispatchers.main) {
                    val currIsMenuOpen = isMenuOpenCallback.invoke()
                    if(isMenuOpen != currIsMenuOpen) {
                        isMenuOpen = currIsMenuOpen
                        changeFabIcon(isMenuOpen)
                    }
                }
            }
        }
    }

    fun stopTimer() {
        job?.cancel()
    }

    private fun changeFabIcon(isMenuOpen: Boolean) {
        icFab.setImage(if(isMenuOpen) IconUnify.CLOSE else IconUnify.ADD)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }

    companion object {
        private const val ANIMATION_DURATION = 200L
        private const val TIMER_CHECK_MENU_STATUS_DURATION = 100L
    }
}