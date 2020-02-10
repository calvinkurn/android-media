package com.tokopedia.home_page_banner.presentation.widgets.circularViewPager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.tokopedia.home_page_banner.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs


/**
 * A ViewPager that auto-scrolls, and supports infinite scroll.
 * For infinite Scroll, you may use LoopingPagerAdapter.
 */
@Suppress("unused")
@SuppressLint("SyntheticAccessor")
open class CircularViewPager : FrameLayout, CoroutineScope{
    companion object{
        const val SCROLL_STATE_DRAGGING = 1
    }

    var isInfinite = true
    var isAutoScroll = false
    var aspectRatio = 0f
    open var itemAspectRatio = 0f
    private var listener: CircularPageChangeListener? = null
    private var adapter: CircularViewPagerAdapter? = null
    private val viewPager: ViewPager2 = ViewPager2(context)
    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main
    //AutoScroll
    private var interval = 5000
    private var previousPosition = 0
    private var currentPagePosition = 0
    private var isAutoScrollResumed = false
    private fun autoScrollLauncher() = launch(coroutineContext) {
        delay(interval.toLong())
        autoScrollCoroutine()
    }

    private suspend fun autoScrollCoroutine() = withContext(Dispatchers.Main){
        if ((adapter != null) || isAutoScroll || (adapter?.itemCount ?: 0 >= 2)) {
            if (!isInfinite && adapter!!.itemCount - 1 == currentPagePosition) {
                currentPagePosition = 0
            } else {
                currentPagePosition++
            }
            viewPager.setCurrentItem(currentPagePosition, true)
        }
    }

    //For Indicator
    private var indicatorPageChangeListener: IndicatorPageChangeListener? = null
    private var previousScrollState = SCROLL_STATE_IDLE
    private var scrollState = SCROLL_STATE_IDLE
    private var isToTheRight = true
    /**
     * This boolean indicates whether LoopingViewPager needs to continuously tell the indicator about
     * the progress of the scroll, even after onIndicatorPageChange().
     * If indicator is smart, it should be able to finish the animation by itself after we told it that a position has been selected.
     * If indicator is not smart, then LoopingViewPager will continue to fire onIndicatorProgress() to update the indicator
     * transition position.
     */
    private var isIndicatorSmart = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CircularViewPager, 0, 0)
        try {
            isInfinite = a.getBoolean(R.styleable.CircularViewPager_isInfinite, false)
            isAutoScroll = a.getBoolean(R.styleable.CircularViewPager_autoScroll, false)
            interval = a.getInt(R.styleable.CircularViewPager_scrollInterval, 5000)
            isAutoScrollResumed = isAutoScroll
        } finally {
            a.recycle()
        }
        init()
    }

    protected fun init() {
        viewPager.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        viewPager.animation = null
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var currentPosition = 0f
            override fun onPageSelected(position: Int) {
                previousPosition = currentPagePosition
                currentPagePosition = position
                if (indicatorPageChangeListener != null) {
                    indicatorPageChangeListener!!.onIndicatorPageChange(indicatorPosition)
                }
                if (isAutoScrollResumed) {
                    masterJob.cancelChildren()
                    autoScrollLauncher()
                }
                listener?.onPageScrolled(position)
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (indicatorPageChangeListener == null) return
                isToTheRight = position + positionOffset >= currentPosition
                if (positionOffset == 0f) currentPosition = position.toFloat()
                val realPosition = getSelectingIndicatorPosition(isToTheRight)
                val progress: Float
                progress = if (scrollState == SCROLL_STATE_SETTLING && abs(currentPagePosition - previousPosition) > 1) {
                    val pageDiff = abs(currentPagePosition - previousPosition)
                    if (isToTheRight) {
                        (position - previousPosition).toFloat() / pageDiff + positionOffset / pageDiff
                    } else {
                        (previousPosition - (position + 1)).toFloat() / pageDiff + (1 - positionOffset) / pageDiff
                    }
                } else {
                    if (isToTheRight) positionOffset else 1 - positionOffset
                }
                if (progress == 0f || progress > 1) return
                if (isIndicatorSmart) {
                    if (scrollState != SCROLL_STATE_DRAGGING) return
                    indicatorPageChangeListener!!.onIndicatorProgress(realPosition, progress)
                } else {
                    if (scrollState == SCROLL_STATE_DRAGGING) {
                        if (isToTheRight && abs(realPosition - currentPagePosition) == 2 ||
                                !isToTheRight && realPosition == currentPagePosition) { //If this happens, it means user is fast scrolling where onPageSelected() is not fast enough
//to catch up with the scroll, thus produce wrong position value.
                            return
                        }
                    }
                    indicatorPageChangeListener!!.onIndicatorProgress(realPosition, progress)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                listener?.onPageScrollStateChanged(state)
                if (!isIndicatorSmart) {
                    if (scrollState == SCROLL_STATE_SETTLING && state == SCROLL_STATE_DRAGGING) {
                        indicatorPageChangeListener?.onIndicatorProgress(getSelectingIndicatorPosition(isToTheRight), 1f)
                    }
                }
                previousScrollState = scrollState
                scrollState = state
                if (state == SCROLL_STATE_IDLE) { // Below are code to achieve infinite scroll.
//We silently and immediately flip the item to the first / last.
                    if (isInfinite) {
                        resumeAutoScroll()
                        if (adapter == null) return
                        val itemCount = adapter!!.itemCount
                        if (itemCount < 2) {
                            return
                        }
                        val index = viewPager.currentItem
                        if (index == 0) {
                            viewPager.setCurrentItem(itemCount - 2, false) //Real last item
                        } else if (index == itemCount - 1) {
                            viewPager.setCurrentItem(1, false) //Real first item
                        }
                    }
                    indicatorPageChangeListener?.onIndicatorProgress(indicatorPosition, 1f)
                } else if(state == SCROLL_STATE_DRAGGING){
                    pauseAutoScroll()
                }
            }
        })
        addView(viewPager)
        if (isInfinite) viewPager.setCurrentItem(1, false)
    }

    fun setAdapter(adapter: CircularViewPagerAdapter) {
        this.adapter = adapter
        viewPager.adapter = this.adapter
        if (isInfinite) viewPager.setCurrentItem(1, false)
    }

    fun setItemList(list: List<CircularModel>){
        adapter?.setItemList(list)
        reset()
    }

    fun resumeAutoScroll() {
        if(isAutoScrollResumed) return
        isAutoScrollResumed = true
        autoScrollLauncher()
    }

    fun pauseAutoScroll() {
        isAutoScrollResumed = false
        masterJob.cancelChildren()
    } //Dummy first item is selected. Indicator should be at the first one//Dummy last item is selected. Indicator should be at the last one

    /**
     * A method that helps you integrate a ViewPager Indicator.
     * This method returns the expected position (Starting from 0) of indicators.
     * This method should be used after currentPagePosition is updated.
     */
    val indicatorPosition: Int
        get() = if (!isInfinite) {
            currentPagePosition
        } else {
            if (adapter !is CircularViewPagerAdapter) currentPagePosition
            when (currentPagePosition) {
                0 -> { //Dummy last item is selected. Indicator should be at the last one
                    (adapter as CircularViewPagerAdapter).listCount - 1
                }
                (adapter as CircularViewPagerAdapter).lastItemPosition + 1 -> { //Dummy first item is selected. Indicator should be at the first one
                    0
                }
                else -> {
                    currentPagePosition - 1
                }
            }
        }

    /**
     * A method that helps you integrate a ViewPager Indicator.
     * This method returns the expected position (Starting from 0) of indicators.
     * This method should be used before currentPagePosition is updated, when user is trying to
     * select a different page, i.e. onPageScrolled() is triggered.
     */
    fun getSelectingIndicatorPosition(isToTheRight: Boolean): Int {
        if (scrollState == SCROLL_STATE_SETTLING || scrollState == SCROLL_STATE_IDLE || previousScrollState == SCROLL_STATE_SETTLING && scrollState == SCROLL_STATE_DRAGGING) {
            return indicatorPosition
        }
        val delta = if (isToTheRight) 1 else -1
        return if (isInfinite) {
            if (adapter !is CircularViewPagerAdapter) return currentPagePosition + delta
            if (currentPagePosition == 1 && !isToTheRight) { //Special case for first page to last page
                (adapter as CircularViewPagerAdapter).lastItemPosition - 1
            } else if ((currentPagePosition == (adapter as CircularViewPagerAdapter).lastItemPosition
                            && isToTheRight)) { //Special case for last page to first page
                0
            } else {
                currentPagePosition + delta - 1
            }
        } else {
            currentPagePosition + delta
        }
    }

    /**
     * A method that helps you integrate a ViewPager Indicator.
     * This method returns the expected count of indicators.
     */
    val indicatorCount: Int
        get() {
            return if (adapter is CircularViewPagerAdapter) {
                (adapter as CircularViewPagerAdapter).listCount
            } else {
                adapter?.itemCount ?: 0
            }
        }

    /**
     * This function needs to be called if dataSet has changed,
     * in order to reset current selected item and currentPagePosition and autoPageSelectionLock.
     */
    fun reset() {
        currentPagePosition = if (isInfinite) {
            viewPager.setCurrentItem(1, false)
            1
        } else {
            viewPager.setCurrentItem(0, false)
            0
        }
    }

    fun setIndicatorSmart(isIndicatorSmart: Boolean) {
        this.isIndicatorSmart = isIndicatorSmart
    }

    fun setIndicatorPageChangeListener(callback: IndicatorPageChangeListener?) {
        indicatorPageChangeListener = callback
    }

    fun setPageChangeListener(pageChangeListener: CircularPageChangeListener){
        this.listener = pageChangeListener
    }

    interface IndicatorPageChangeListener {
        fun onIndicatorProgress(selectingPosition: Int, progress: Float)
        fun onIndicatorPageChange(newIndicatorPosition: Int)
    }

    fun setInterval(interval: Int) {
        this.interval = interval
        resetAutoScroll()
    }

    fun getViewPager() = viewPager

    private fun resetAutoScroll() {
        pauseAutoScroll()
        resumeAutoScroll()
    }
}