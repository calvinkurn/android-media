package com.tokopedia.officialstore.category.presentation.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
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

    private var animationExpand: ValueAnimator ?= null
    private var animationCollapse: ValueAnimator ?= null

    private var isExpand = true

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
                tab.customView?.apply {
                    image_view_category_icon.loadImageWithCache(categoriesItemTab[tab.position].iconUrl)
                    text_view_category_title?.apply {
                        setTextColor(MethodChecker.getColor(
                                context,
                                R.color.Unify_P600
                        ))
                        setWeight(Typography.BOLD)
                    }
                }
            }

            override fun onTabUnselected(tab: Tab) {
                tab.customView?.apply {
                    image_view_category_icon.loadImageWithCache(categoriesItemTab[tab.position].inactiveIconUrl)
                    text_view_category_title?.apply {
                        setTextColor(MethodChecker.getColor(
                                context,
                                R.color.Unify_N700_96
                        ))
                        setWeight(Typography.REGULAR)
                    }
                }
            }

            override fun onTabSelected(tab: Tab) {
                tab.customView?.apply {
                    image_view_category_icon.loadImageWithCache(categoriesItemTab[tab.position].iconUrl)
                    text_view_category_title?.apply {
                        setTextColor(MethodChecker.getColor(
                                context,
                                R.color.Unify_P600
                        ))
                        setWeight(Typography.BOLD)
                    }
                }
                expandAllTab()
            }
        })

        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            tab?.customView = getTabView(context, i)
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

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
            collapseAllTab()
        } else {
            expandAllTab()
        }
    }

    private fun startTabHeightExpandAnimation(appBarLayout: AppBarLayout) {
        expandAllTab()
        if (layoutParams.height != tabMaxHeight) {
            appBarLayout.setExpanded(true)
        }
    }

    private fun expandAllTab() {
        if(animationExpand == null)
            animationExpand = getValueAnimator(32.dp.toFloat(), 64.dp.toFloat(), 300, AccelerateDecelerateInterpolator()) {
                if (this.layoutParams != null) {
                    val params: ViewGroup.LayoutParams = layoutParams
                    params.height = it.toInt()
                    requestLayout()
                }
            }
        if(this.measuredHeight == 32.dp) {
            animationExpand?.start()
            isExpand = true
            for (i in 0 until tabCount) {

                val icon = findViewByIdFromTab(getTabAt(i), R.id.motion_layout)
                val container = findViewByIdFromTab(getTabAt(i), R.id.tab_category_container)
                if (icon is MotionLayout) {
                    icon.setTransitionListener(object : MotionLayout.TransitionListener {
                        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

                        override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, progress: Float) {}

                        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}

                        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
                    })
                    icon.transitionToStart()
                }
            }
        }

    }

    private fun collapseAllTab() {
        if(animationCollapse == null) animationCollapse =
            getValueAnimator(64.dp.toFloat(), 32.dp.toFloat(), 300, AccelerateDecelerateInterpolator()) {
                if (this.layoutParams != null) {
                    val params: ViewGroup.LayoutParams = layoutParams
                    params.height = it.toInt()
                    requestLayout()
                }
            }
        if(this.measuredHeight == 64.dp) {
            animationCollapse?.start()
            isExpand = false
            for (i in 0 until tabCount) {
                val icon = findViewByIdFromTab(getTabAt(i), R.id.motion_layout)
                if (icon is MotionLayout) {
                    icon.setTransitionListener(object : MotionLayout.TransitionListener {
                        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

                        override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, progress: Float) {}

                        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                        }

                        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                        }
                    })
                    icon.transitionToEnd()
                }
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

    private fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_official_store_category, null)
        with(view){
            ImageHandler.loadImage(
                    context,
                    image_view_category_icon,
                    categoriesItemTab[position].inactiveIconUrl,
                    R.drawable.ic_loading_image
            )
            text_view_category_title.text = categoriesItemTab[position].title
            tab_category_container?.afterMeasured {
                categoriesItemTab[position].currentWidth = width
                categoriesItemTab[position].minWidth = width
                categoriesItemTab[position].maxWidth = width + 32.dp

                categoriesItemTab[position].currentHeight = height
                categoriesItemTab[position].minHeight = height - 32.dp
                categoriesItemTab[position].maxHeight = height

            }
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

    class CategoriesItemTab(
            val title: String,
            val iconUrl: String,
            val inactiveIconUrl: String,
            var currentWidth: Int = 64.dp,
            var currentHeight: Int = 64.dp,
            var minWidth: Int = 64.dp,
            var maxWidth: Int = 64.dp,
            var minHeight: Int = 64.dp,
            var maxHeight: Int = 64.dp
    )
}