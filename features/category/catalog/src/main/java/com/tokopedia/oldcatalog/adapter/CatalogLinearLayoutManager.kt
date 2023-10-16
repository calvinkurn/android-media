package com.tokopedia.oldcatalog.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailPageFragment

/***
This class handles the animation of [Scroll to Products] and [Scroll to Top] buttons.
 ***/
class CatalogLinearLayoutManager(val context : Context, orientation : Int, reverseLayout: Boolean)
    : LinearLayoutManager(context, orientation,reverseLayout) {

    private var userPressedLastTopPosition : Int  = 0
    var lastComponentIndex = 0
    private var animationListener : CatalogAnimationListener? = null
    var slideDownTimer : Runnable? = null
    private val animationHandler = Handler(Looper.getMainLooper())

    companion object {
        const val MILLI_SECONDS_PER_INCH_BOTTOM_SCROLL = 250f
        const val MILLI_SECONDS_PER_INCH_TOP_SCROLL = 250f
        const val MILLI_SECONDS_FOR_UI_THREAD = 100L
        const val MILLI_SECONDS_FOR_MORE_PRODUCTS_VIEW = 2000L
        const val OFFSET_FOR_PRODUCT_SECTION_SNAP = 300
        const val MINIMUM_SCROLL_FOR_ANIMATION = 5
    }
    private val smoothScrollerToBottom =  object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return if (displayMetrics != null) {
                return MILLI_SECONDS_PER_INCH_BOTTOM_SCROLL /displayMetrics.densityDpi
            } else
                super.calculateSpeedPerPixel(displayMetrics)
        }
    }

    private val smoothScrollerToTop =  object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return if (displayMetrics != null) {
                return MILLI_SECONDS_PER_INCH_TOP_SCROLL /displayMetrics.densityDpi
            } else
                super.calculateSpeedPerPixel(displayMetrics)
        }
    }

    fun setCatalogAnimationListener(listener: CatalogAnimationListener){
        animationListener = listener
    }

    private val scrollToTopPositionRunnable = Runnable {
        scrollToPositionWithOffset(userPressedLastTopPosition, -OFFSET_FOR_PRODUCT_SECTION_SNAP)
        animationHandler.postDelayed(smoothScrollToTopPositionRunnable,
            MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    private val smoothScrollToTopPositionRunnable = Runnable {
        smoothScrollerToTop.targetPosition = userPressedLastTopPosition
        startSmoothScroll(smoothScrollerToTop)
        animationListener?.setIsScrollButtonDown(false)
    }

    fun scrollToTop() {
        animationHandler.postDelayed(scrollToTopPositionRunnable,
            MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    fun scrollToBottom(lastTopPosition : Int) {
        userPressedLastTopPosition = lastTopPosition
        animationHandler.postDelayed(scrollToBottomPositionRunnable,
            MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    private val scrollToBottomPositionRunnable = Runnable {
        scrollToPositionWithOffset(lastComponentIndex,
            OFFSET_FOR_PRODUCT_SECTION_SNAP
        )
        animationHandler.postDelayed(smoothScrollToBottomPositionRunnable,
            MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    private val smoothScrollToBottomPositionRunnable = Runnable {
        smoothScrollerToBottom.targetPosition = lastComponentIndex
        startSmoothScroll(smoothScrollerToBottom)
    }

    fun removeOldAnimation(slideDownFunction : () -> Unit) {
        if(slideDownTimer != null)
            animationHandler.removeCallbacks(slideDownTimer!!)
        val newSlideDownTimer  =  Runnable {
            slideDownFunction.invoke()
        }
        slideDownTimer = newSlideDownTimer
        animationHandler.postDelayed(newSlideDownTimer,
            MILLI_SECONDS_FOR_MORE_PRODUCTS_VIEW
        )
    }

    fun getSlideUpAnimation() : Animation{
        return AnimationUtils.loadAnimation(context, R.anim.slide_up)
    }

    fun getProductCountViewSlideDownAnimation() : Animation {
        return AnimationUtils.loadAnimation(context, R.anim.slide_down_products_view)
    }

    fun getSlideDownAnimation() : Animation{
        return AnimationUtils.loadAnimation(context, R.anim.slide_down)
    }

    fun removeAllHandlers() {
        animationHandler.removeCallbacks(scrollToBottomPositionRunnable)
        animationHandler.removeCallbacks(scrollToTopPositionRunnable)
        animationHandler.removeCallbacks(smoothScrollToBottomPositionRunnable)
        animationHandler.removeCallbacks(smoothScrollToTopPositionRunnable)
    }
}

interface CatalogAnimationListener{
    fun setIsScrollButtonDown(value : Boolean)
}
