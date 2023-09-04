package com.tokopedia.feedcomponent.view.custom

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.feedcomponent.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created By : Jonathan Darwin on June 27, 2022
 */
class FeedFloatingButton : LinearLayout, View.OnClickListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    /** View */
    private val flTextWrapper: FrameLayout
    private val icFab: IconUnify
    private val tvFab: Typography

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, com.tokopedia.content.common.R.styleable.FeedFloatingButton)

             tvFab.text = attributeArray.getString(com.tokopedia.content.common.R.styleable.FeedFloatingButton_fab_text) ?: context.getString(com.tokopedia.content.common.R.string.feed_fab_create_content)
            attributeArray.recycle()
        }
    }

    init {
        val view = View.inflate(context, R.layout.view_feed_floating_button, this)
        super.setOnClickListener(this)

        flTextWrapper = view.findViewById(R.id.fl_fab_text_wrapper)
        icFab = view.findViewById(R.id.ic_fab)
        tvFab = view.findViewById(R.id.tv_fab)
    }

    private var mOnClickListener: OnClickListener? = null
    private var isExpand: Boolean = false
    private var isMenuOpen: Boolean = false

    private val dispatchers = CoroutineDispatchersProvider
    private var job: Job? = null
    private val scope = CoroutineScope(dispatchers.computation)

    var isShrinkOnClick: Boolean = true

    override fun onClick(p0: View?) {
        if(isShrinkOnClick) shrink()

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
