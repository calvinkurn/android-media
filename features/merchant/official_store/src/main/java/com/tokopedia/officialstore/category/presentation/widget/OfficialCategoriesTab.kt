package com.tokopedia.officialstore.category.presentation.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.officialstore.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.*


class OfficialCategoriesTab(context: Context,
                            attributes: AttributeSet) : TabLayout(context, attributes) {

    private val categoriesItemTab = ArrayList<CategoriesItemTab>()
    private var itemTabMap = mutableMapOf<String, Int>()

    private var totalScrollUp: Int = 0
    private var tabMaxHeight: Int = 0
    private var tabMinHeight: Int = 0
    private var viewMaxHeight: Int = 0

    private var animationExpand: ValueAnimator ?= null
    private var animationCollapse: ValueAnimator ?= null

    private var isExpand = true

    fun setMeasuredHeight() {
        viewMaxHeight = this.measuredHeight
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setup(viewPager: ViewPager, tabItemDataList: List<CategoriesItemTab>) {
        this.categoriesItemTab.clear()
        this.categoriesItemTab.addAll(tabItemDataList)
        tabItemDataList.forEachIndexed{index, item ->
            itemTabMap.put(item.categoryId, index)
        }
        initResources()
        clearOnTabSelectedListeners()
        viewPager.clearOnPageChangeListeners()
        setupWithViewPager(viewPager)
        setupListener()

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
            animationExpand = getValueAnimator(32.dp.toFloat(), viewMaxHeight.toFloat(), 300, AccelerateDecelerateInterpolator()) {
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
            getValueAnimator(viewMaxHeight.toFloat(), 32.dp.toFloat(), 300, AccelerateDecelerateInterpolator()) {
                if (this.layoutParams != null) {
                    val params: ViewGroup.LayoutParams = layoutParams
                    params.height = it.toInt()
                    requestLayout()
                }
            }
        if(this.measuredHeight >= viewMaxHeight) {
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

    private fun setupListener() {

        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab) {
                tab.customView?.apply {
                    this.findViewById<ImageView>(R.id.image_view_category_icon)?.loadImageWithCache(categoriesItemTab[tab.position].iconUrl)
                    this.findViewById<Typography>(R.id.text_view_category_title)?.apply {
                        setTextColor(
                            MethodChecker.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_P600
                            )
                        )
                        setWeight(Typography.BOLD)
                    }
                }
            }

            override fun onTabUnselected(tab: Tab) {
                tab.customView?.apply {
                    this.findViewById<ImageView>(R.id.image_view_category_icon)?.loadImageWithCache(categoriesItemTab[tab.position].inactiveIconUrl)
                    this.findViewById<Typography>(R.id.text_view_category_title)?.apply {
                        setTextColor(
                            MethodChecker.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                            )
                        )
                        setWeight(Typography.REGULAR)
                    }
                }
            }

            override fun onTabSelected(tab: Tab) {
                tab.customView?.apply {
                    this.findViewById<ImageUnify>(R.id.image_view_category_icon)?.loadImageWithCache(categoriesItemTab[tab.position].iconUrl)
                    this.findViewById<Typography>(R.id.text_view_category_title)?.apply {
                        setTextColor(
                            MethodChecker.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_P600
                            )
                        )
                        setWeight(Typography.BOLD)
                    }
                }
                expandAllTab()
            }
        })
    }

    @SuppressLint("ResourcePackage")
    private fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_official_store_category, null)
        with(view) {
            val image_view_category_icon =
                view?.findViewById<ImageUnify>(R.id.image_view_category_icon)
            val text_view_category_title =
                view?.findViewById<Typography>(R.id.text_view_category_title)
            val tab_category_container =
                view?.findViewById<FrameLayout>(R.id.tab_category_container)

            ImageHandler.loadImage(
                context,
                image_view_category_icon,
                categoriesItemTab[position].inactiveIconUrl,
                R.drawable.ic_loading_image
            )
            text_view_category_title?.text = categoriesItemTab[position].title
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

    fun getPositionBasedOnCategoryId(categoryId: String): Int {
        return try {
            itemTabMap.getValue(categoryId)
        } catch (e: Exception) {
            -1
        }
    }

    class CategoriesItemTab(
            var categoryId: String = "0",
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
