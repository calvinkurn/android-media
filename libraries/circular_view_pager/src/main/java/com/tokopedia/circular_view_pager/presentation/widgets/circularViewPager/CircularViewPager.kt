package com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.tokopedia.home_page_banner.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

@Suppress("unused")
@SuppressLint("SyntheticAccessor")
open class CircularViewPager : FrameLayout, CoroutineScope{
    companion object{
        const val SCROLL_STATE_DRAGGING = 1
    }

    var isInfinite = true
    var isAutoScroll = false
    var isPageTransformer = false
    var aspectRatio = 0f
    var pageMargin = 0
    var offset = 0
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

    // tracker
    private val impressionStatusList = mutableListOf<Boolean>()

    private suspend fun autoScrollCoroutine() = withContext(Dispatchers.Main){
        if ((adapter != null) || isAutoScroll || (adapter?.itemCount ?: 0 >= 2)) {
            if (!isInfinite && (adapter?.itemCount ?: 0) - 1 == currentPagePosition) {
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

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CircularViewPager, 0, 0)
        try {
            isInfinite = a.getBoolean(R.styleable.CircularViewPager_isInfinite, false)
            isAutoScroll = a.getBoolean(R.styleable.CircularViewPager_autoScroll, false)
            isPageTransformer = a.getBoolean(R.styleable.CircularViewPager_activePageTransformer, false)
            pageMargin = a.getDimensionPixelOffset(R.styleable.CircularViewPager_pageMargin, 0)
            offset = a.getDimensionPixelOffset(R.styleable.CircularViewPager_offset, 0)
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
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            var currentPosition = 0f
            override fun onPageSelected(position: Int) {
                previousPosition = currentPagePosition
                currentPagePosition = position
                indicatorPageChangeListener?.onIndicatorPageChange(indicatorPosition)
                if (isAutoScrollResumed) {
                    masterJob.cancelChildren()
                    autoScrollLauncher()
                }
                setImpression(indicatorPosition)
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
                if (scrollState == SCROLL_STATE_DRAGGING) {
                    if (isToTheRight && abs(realPosition - currentPagePosition) == 2 ||
                            !isToTheRight && realPosition == currentPagePosition) {
                        //If this happens, it means user is fast scrolling where onPageSelected() is not fast enough
                        //to catch up with the scroll, thus produce wrong position value.
                        return
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                listener?.onPageScrollStateChanged(state)
                previousScrollState = scrollState
                scrollState = state
                if (state == SCROLL_STATE_IDLE) {
                    // Below are code to achieve infinite scroll.
                    //We silently and immediately flip the item to the first / last.
                    if (isInfinite) {
                        resumeAutoScroll()
                        if (adapter == null) return
                        val itemCount = adapter?.itemCount ?: 0
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
                } else if(state == SCROLL_STATE_DRAGGING){
                    pauseAutoScroll()
                }
            }
        })
        if(isPageTransformer){
            with(viewPager) {
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
            }

            viewPager.setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * pageMargin + offset)
                if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
        }
        addView(viewPager)
        if (isInfinite) viewPager.setCurrentItem(1, false)
    }

    fun setAdapter(adapter: CircularViewPagerAdapter) {
        this.adapter = adapter
        this.adapter?.setIsInfinite(this.isInfinite)
        viewPager.adapter = this.adapter
        if (isInfinite && adapter.listCount > 0) viewPager.setCurrentItem(1, false)
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
    }

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
        resetImpressions()
        resetScrollToStart()
        /**
         * Position is reset to pos 0, therefore impression in pos 0
         * should be called
         */
        setImpression(0)
    }

    fun resetScrollToStart() {
        currentPagePosition = if (isInfinite) {
            viewPager.setCurrentItem(1, false)
            1
        } else {
            viewPager.setCurrentItem(0, false)
            0
        }
        resetAutoScroll()
    }

    fun resetImpressions(){
        impressionStatusList.clear()
        for (i in 0..(adapter?.listCount ?: 0)) {
            impressionStatusList.add(false)
        }
    }

    private fun setImpression(position: Int){
        val realCount = adapter?.listCount ?: 0
        if (position != -1 && realCount != 0 && position < realCount && impressionStatusList.size > position && !impressionStatusList[position]) {
            impressionStatusList[position] = true
            listener?.onPageScrolled(position)
        }
    }

    fun setIndicatorPageChangeListener(callback: IndicatorPageChangeListener?) {
        indicatorPageChangeListener = callback
    }

    fun setPageChangeListener(pageChangeListener: CircularPageChangeListener){
        this.listener = pageChangeListener
    }

    interface IndicatorPageChangeListener {
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resumeAutoScroll()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pauseAutoScroll()
    }
}