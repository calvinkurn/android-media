package com.tokopedia.catalog.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment

class CatalogLinearLayoutManager(val context : Context, orientation : Int, reverseLayout: Boolean)
    : LinearLayoutManager(context, orientation,reverseLayout) {

    private var userPressedLastTopPosition : Int  = 0
    var lastComponentIndex = 0
    private var animationListener : CatalogAnimationListener? = null

    private val smoothScrollerToBottom =  object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return if (displayMetrics != null) {
                return CatalogDetailPageFragment.MILLI_SECONDS_PER_INCH_BOTTOM_SCROLL /displayMetrics.densityDpi
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
                return CatalogDetailPageFragment.MILLI_SECONDS_PER_INCH_TOP_SCROLL /displayMetrics.densityDpi
            } else
                super.calculateSpeedPerPixel(displayMetrics)
        }
    }

    fun setCatalogAnimationListener(listener: CatalogAnimationListener){
        animationListener = listener
    }

    private val animationHandler = Handler(Looper.getMainLooper())

    private val scrollToTopPositionRunnable = Runnable {
        scrollToPositionWithOffset(userPressedLastTopPosition, -CatalogDetailPageFragment.OFFSET_FOR_PRODUCT_SECTION_SNAP)
        animationHandler.postDelayed(smoothScrollToTopPositionRunnable,
            CatalogDetailPageFragment.MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    private val smoothScrollToTopPositionRunnable = Runnable {
        smoothScrollerToTop.targetPosition = userPressedLastTopPosition
        startSmoothScroll(smoothScrollerToTop)
        animationListener?.setIsScrollButtonDown(false)
    }

    fun scrollToTop() {
        animationHandler.postDelayed(scrollToTopPositionRunnable,
            CatalogDetailPageFragment.MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    fun scrollToBottom(lastTopPosition : Int) {
        userPressedLastTopPosition = lastTopPosition
        animationHandler.postDelayed(scrollToBottomPositionRunnable,
            CatalogDetailPageFragment.MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    private val scrollToBottomPositionRunnable = Runnable {
        scrollToPositionWithOffset(lastComponentIndex,
            CatalogDetailPageFragment.OFFSET_FOR_PRODUCT_SECTION_SNAP
        )
        animationHandler.postDelayed(smoothScrollToBottomPositionRunnable,
            CatalogDetailPageFragment.MILLI_SECONDS_FOR_UI_THREAD
        )
    }

    private val smoothScrollToBottomPositionRunnable = Runnable {
        smoothScrollerToBottom.targetPosition = lastComponentIndex
        startSmoothScroll(smoothScrollerToBottom)
    }

    var slideDownTimer : Runnable? = null

    fun removeOldAnimation(slideDownFunction : () -> Unit) {
        if(slideDownTimer != null)
            animationHandler.removeCallbacks(slideDownTimer!!)
        val newSlideDownTimer  =  Runnable {
            slideDownFunction.invoke()
        }
        slideDownTimer = newSlideDownTimer
        animationHandler.postDelayed(newSlideDownTimer,
            CatalogDetailPageFragment.MILLI_SECONDS_FOR_MORE_PRODUCTS_VIEW
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