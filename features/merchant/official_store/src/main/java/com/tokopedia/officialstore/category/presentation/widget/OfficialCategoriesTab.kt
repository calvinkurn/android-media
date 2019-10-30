package com.tokopedia.officialstore.category.presentation.widget

import android.animation.ValueAnimator
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.officialstore.R
import kotlinx.android.synthetic.main.view_official_store_category.view.*
import java.util.*

class OfficialCategoriesTab(context: Context, attributes: AttributeSet) : TabLayout(context, attributes) {

    private val DEFAULT_ANIMATION_DURATION: Long = 300
    private val MAX_TAB_COLLAPSE_SCROLL_RANGE = 200
    private val SCROLL_UP_THRESHOLD_BEFORE_EXPAND = 500

    private val categoriesItemTab = ArrayList<CategoriesItemTab>()

    private var tabMaxHeight: Int = 0
    private var tabMinHeight: Int = 0

    private var totalScrollUp: Int = 0
    private var scrollEnabled = false
    private var lastTabCollapseFraction = 0f

    private var tabHeightCollapseAnimator: ValueAnimator? = null

    fun setup(viewPager: ViewPager, tabItemDataList: List<CategoriesItemTab>) {
        this.categoriesItemTab.clear()
        this.categoriesItemTab.addAll(tabItemDataList)
        resetAllState()
        initResources()
        initAnimator()
        isSmoothScrollingEnabled = true
        clearOnTabSelectedListeners()
        viewPager.clearOnPageChangeListeners()
        setupWithViewPager(viewPager)

        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab) {
                tab.customView?.apply {
                    text_view_category_title?.setTextColor(MethodChecker.getColor(
                            context,
                            R.color.Purple_P600
                    ))
                }
            }

            override fun onTabUnselected(tab: Tab) {
                tab.customView?.apply {
                    text_view_category_title?.setTextColor(MethodChecker.getColor(
                            context,
                            R.color.Neutral_N700_96
                    ))
                }
            }

            override fun onTabSelected(tab: Tab) {
                tab.customView?.apply {
                    text_view_category_title?.setTextColor(MethodChecker.getColor(
                            context,
                            R.color.Purple_P600
                    ))
                }
            }
        })

        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            tab?.customView = getTabView(context, i)
        }
    }

    private fun resetAllState() {
        lastTabCollapseFraction = 0f
        scrollEnabled = false
        totalScrollUp = 0
    }

    private fun initResources() {
        tabMaxHeight = resources.getDimensionPixelSize(R.dimen.layout_lvl7)
        tabMinHeight = resources.getDimensionPixelSize(R.dimen.layout_lvl6)
    }

    private fun initAnimator() {
        tabHeightCollapseAnimator = ValueAnimator.ofInt(tabMaxHeight, tabMinHeight)
        tabHeightCollapseAnimator?.addUpdateListener { valueAnimator ->
            if (isFullyCollapsed(valueAnimator.animatedFraction)) {
                hideIconForAllTabs()
            } else {
                showIconForAllTabs()
            }
            adjustTabLayoutHeight(valueAnimator.animatedValue as Int)
        }
        tabHeightCollapseAnimator?.duration = DEFAULT_ANIMATION_DURATION
    }

    private fun isFullyCollapsed(fraction: Float): Boolean {
        return fraction >= 1
    }

    private fun showIconForAllTabs() {
        for (i in 0 until tabCount) {
            val icon = findViewByIdFromTab(getTabAt(i), R.id.image_view_category_icon)
            if (icon is ImageView) {
                icon.visibility = View.VISIBLE
            }
        }
    }

    private fun hideIconForAllTabs() {
        for (i in 0 until tabCount) {
            val icon = findViewByIdFromTab(getTabAt(i), R.id.image_view_category_icon)
            if (icon is ImageView) {
                icon.visibility = View.GONE
            }
        }
    }

    private fun findViewByIdFromTab(tab: Tab?, id: Int): View? {
        return if (tab != null && tab.customView != null) {
            tab.customView!!.findViewById(id)
        } else {
            null
        }
    }

    private fun adjustTabLayoutHeight(height: Int) {
        if (layoutParams.height != height) {
            layoutParams.height = height
            requestLayout()
        }
    }

    private fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_official_store_category,null)
        with(view){
            ImageHandler.loadImage(
                    context,
                    image_view_category_icon,
                    categoriesItemTab[position].iconUrl,
                    R.drawable.ic_loading_image
            )

            var categoryName = categoriesItemTab[position].title
            val maxLength = if (categoryName.length < 12)
                categoryName.length else 12
            categoryName = categoryName.substring(0, maxLength)
            text_view_category_title.text = if (maxLength == 12) "${categoryName.trim()}..." else categoryName
        }
        return view
    }

    private fun getTabCollapseDeltaFraction(dy: Int): Float {
        return dy.toFloat() / MAX_TAB_COLLAPSE_SCROLL_RANGE
    }

    private fun adjustTabCollapseFraction(tabCollapseFraction: Float) {
        tabHeightCollapseAnimator?.let {
            if (it.isRunning) {
                it.cancel()
            }
            setCurrentFraction(it, tabCollapseFraction)
            lastTabCollapseFraction = tabCollapseFraction
        }
    }

    private fun setCurrentFraction(animator: ValueAnimator, fraction: Float) {
        animator.currentPlayTime = (fraction * animator.duration).toLong()
    }

    fun adjustTabCollapseOnScrolled(dy: Int, totalScrollY: Int) {

        if (dy == 0) {
            return
        }

        if (dy < 0) {
            totalScrollUp -= dy
        } else {
            totalScrollUp = 0
        }

        if (dy < 0 && totalScrollY > MAX_TAB_COLLAPSE_SCROLL_RANGE && totalScrollUp < SCROLL_UP_THRESHOLD_BEFORE_EXPAND) {
            return
        }

        val updatedFraction = lastTabCollapseFraction + getTabCollapseDeltaFraction(dy)

        if (updatedFraction > 1) {
            adjustTabCollapseFraction(1f)
        } else if (updatedFraction < 0) {
            adjustTabCollapseFraction(0f)
        } else {
            adjustTabCollapseFraction(updatedFraction)
        }
    }

    class CategoriesItemTab(val title: String, val iconUrl: String)
}