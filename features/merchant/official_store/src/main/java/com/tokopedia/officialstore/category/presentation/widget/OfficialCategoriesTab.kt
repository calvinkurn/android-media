package com.tokopedia.officialstore.category.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.officialstore.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.view_official_store_category.view.*
import java.util.*

class OfficialCategoriesTab(context: Context,
                            attributes: AttributeSet) : TabLayout(context, attributes) {

    private val categoriesItemTab = ArrayList<CategoriesItemTab>()

    private var totalScrollUp: Int = 0
    private var tabMaxHeight: Int = 0
    private var tabMinHeight: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    fun setup(viewPager: ViewPager, tabItemDataList: List<CategoriesItemTab>) {
        this.categoriesItemTab.clear()
        this.categoriesItemTab.addAll(tabItemDataList)
        initResources()
        clearOnTabSelectedListeners()
        viewPager.clearOnPageChangeListeners()
        setupWithViewPager(viewPager)

        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab) {
                text_view_category_title?.apply {
                    setTextColor(MethodChecker.getColor(
                            context,
                            R.color.Purple_P600
                    ))
                    setWeight(Typography.BOLD)
                }
            }

            override fun onTabUnselected(tab: Tab) {
                tab.customView?.apply {
                    text_view_category_title?.apply {
                        setTextColor(MethodChecker.getColor(
                                context,
                                R.color.Neutral_N700_96
                        ))
                        setWeight(Typography.REGULAR)
                    }
                }
            }

            override fun onTabSelected(tab: Tab) {
                tab.customView?.apply {
                    text_view_category_title?.apply {
                        setTextColor(MethodChecker.getColor(
                                context,
                                R.color.Purple_P600
                        ))
                        setWeight(Typography.BOLD)
                    }
                }
            }
        })

        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            tab?.customView = getTabView(context, i)
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                startTabHeightExpandAnimation()
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun initResources() {
        tabMaxHeight = resources.getDimensionPixelSize(R.dimen.os_tab_max_height)
        tabMinHeight = resources.getDimensionPixelSize(R.dimen.os_tab_min_height)
    }

    private fun adjustCollapseExpandTab(isCollapse: Boolean) {
        if (isCollapse) {
            hideIconForAllTabs()
            adjustTabLayoutHeight(tabMinHeight)
        } else {
            showIconForAllTabs()
            adjustTabLayoutHeight(tabMaxHeight)
        }
    }

    private fun startTabHeightExpandAnimation() {
        showIconForAllTabs()
        adjustTabLayoutHeight(tabMaxHeight)
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
        val view = LayoutInflater.from(context).inflate(R.layout.view_official_store_category, null)
        with(view){
            ImageHandler.loadImage(
                    context,
                    image_view_category_icon,
                    categoriesItemTab[position].iconUrl,
                    R.drawable.ic_loading_image
            )
            text_view_category_title.text = categoriesItemTab[position].title
        }
        return view
    }

    fun adjustTabCollapseOnScrolled(dy: Int) {
        if (dy == 0) {
            return
        }

        if (dy < 0) {
            totalScrollUp -= dy
        } else {
            totalScrollUp = 0
        }

        adjustCollapseExpandTab(totalScrollUp in 0..10)
    }

    class CategoriesItemTab(val title: String, val iconUrl: String)
}