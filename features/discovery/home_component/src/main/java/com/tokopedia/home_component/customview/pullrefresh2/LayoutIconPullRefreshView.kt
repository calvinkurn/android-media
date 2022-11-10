package com.tokopedia.home_component.customview.pullrefresh2

import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import kotlin.math.roundToInt


/**
 * Created by dhaba
 */
class LayoutIconPullRefreshView : ConstraintLayout, LayoutIconPullRefreshListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var containerIconPullRefresh: ConstraintLayout? = null
    private var loaderPullRefresh: LoaderUnify? = null
    private var maxOffset: Int = 0
    private var progressRefresh: Float = 0.0f
    private var offsetY: Float = 0.0f
    private var pullRefreshIcon: ImageUnify? = null
    private var heightLayoutScroll: Int = 0

    companion object {
        private const val MAXIMUM_HEIGHT_SCROLL = 120
        private const val HEIGHT_LOADING = 64
        private const val MAXIMUM_PROGRESS_REFRESH = 1
        private const val TIME_DURATION_ANIMATION_HEIGHT : Long = 300
        private const val HEIGHT_LAYOUT_GONE = 0
    }

    init {
        val view = View.inflate(context, R.layout.layout_icon_pull_refresh_view, this)
        containerIconPullRefresh = view.findViewById(R.id.container_icon_pull_refresh)
        pullRefreshIcon = view.findViewById(R.id.progress_pull_refresh)
        containerIconPullRefresh?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        loaderPullRefresh = view.findViewById(R.id.loader_pull_refresh)
    }

    override fun maxOffsetTop(maxOffsetTop: Int) {
        maxOffset = maxOffsetTop
    }

    override fun offsetView(offset: Float) {
        offsetY = offset
        positionChildren()
        loaderPullRefresh?.gone()
        pullRefreshIcon?.show()
    }

    override fun startRefreshing() {
        containerIconPullRefresh?.visible()
        setLayoutHeight(heightLayoutScroll, HEIGHT_LOADING) {}
        loaderPullRefresh?.visible()
        pullRefreshIcon?.gone()
    }

    override fun stopRefreshing(isAfterRefresh: Boolean) {
        if ((offsetY < maxOffset && progressRefresh < MAXIMUM_PROGRESS_REFRESH) || isAfterRefresh) {
            setLayoutHeight(HEIGHT_LOADING, 0) { containerIconPullRefresh?.gone() }
        }
    }

    private fun setLayoutHeight(currentHeight: Int, targetHeight: Int, callback: () -> Unit) {
        val slideAnimator = ValueAnimator
            .ofInt(currentHeight, targetHeight)
            .setDuration(TIME_DURATION_ANIMATION_HEIGHT)
        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            if (value == HEIGHT_LAYOUT_GONE) {
                callback.invoke()
            }
            containerIconPullRefresh?.layoutParams?.height = value
            containerIconPullRefresh?.requestLayout()
        }
        val set = AnimatorSet()
        set.play(slideAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.start()
    }

    override fun progressRefresh(progress: Float) {
        progressRefresh = progress
        pullRefreshIcon?.scaleX = progress
        pullRefreshIcon?.scaleY = progress
    }

    private fun positionChildren() {
        if (offsetY > HEIGHT_LAYOUT_GONE) {
            heightLayoutScroll = ((offsetY / maxOffset) * MAXIMUM_HEIGHT_SCROLL).roundToInt()
            containerIconPullRefresh?.visible()
            val layoutParams = containerIconPullRefresh?.layoutParams
            layoutParams?.height = heightLayoutScroll
            containerIconPullRefresh?.layoutParams = layoutParams
        }
    }
}
