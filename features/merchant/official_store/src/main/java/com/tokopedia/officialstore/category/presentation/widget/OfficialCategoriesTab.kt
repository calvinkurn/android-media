package com.tokopedia.officialstore.category.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.officialstore.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.view_official_store_category.view.*
import java.util.*
import android.view.*
import com.google.android.material.appbar.AppBarLayout

class OfficialCategoriesTab(context: Context,
                            attributes: AttributeSet) : TabLayout(context, attributes) {

    private val categoriesItemTab = ArrayList<CategoriesItemTab>()

    private var totalScrollUp: Int = 0
    private var tabMaxHeight: Int = 0
    private var tabMinHeight: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    fun setup(viewPager: ViewPager, tabItemDataList: List<CategoriesItemTab>, appBarLayout: AppBarLayout) {
        this.categoriesItemTab.clear()
        this.categoriesItemTab.addAll(tabItemDataList)
        initResources()
        clearOnTabSelectedListeners()
        viewPager.clearOnPageChangeListeners()
        setupWithViewPager(viewPager)

        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab) {
                tab.customView?.apply {
                    ImageHandler.loadImage(
                            context,
                            image_view_category_icon,
                            categoriesItemTab[tab.position].iconUrl,
                            R.drawable.ic_loading_image
                    )
                    text_view_category_title?.apply {
                        setTextColor(MethodChecker.getColor(
                                context,
                                R.color.Purple_P600
                        ))
                        setWeight(Typography.BOLD)
                    }
                }
            }

            override fun onTabUnselected(tab: Tab) {
                tab.customView?.apply {
                    ImageHandler.loadImage(
                            context,
                            image_view_category_icon,
                            categoriesItemTab[tab.position].inactiveIconUrl,
                            R.drawable.ic_loading_image
                    )
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
                    ImageHandler.loadImage(
                            context,
                            image_view_category_icon,
                            categoriesItemTab[tab.position].iconUrl,
                            R.drawable.ic_loading_image
                    )
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
                startTabHeightExpandAnimation(appBarLayout)
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
        } else {
            showIconForAllTabs()
        }
    }

    private fun startTabHeightExpandAnimation(appBarLayout: AppBarLayout) {
        showIconForAllTabs()
        if (layoutParams.height != tabMaxHeight) {
            appBarLayout.setExpanded(true)
        }
    }

    private fun showIconForAllTabs() {
        for (i in 0 until tabCount) {
            val icon = findViewByIdFromTab(getTabAt(i), R.id.image_view_category_icon)
            val textCategory = findViewByIdFromTab(getTabAt(i), R.id.text_view_category_title)
            if (icon is ImageView) {
                // icon.animate().scaleX(1.0f).scaleY(1.0f).duration = 200
                // icon.animate().alpha(1f).duration = 100
                icon.animate().translationY(0f).duration = 100
                textCategory?.animate()?.translationY(0f)?.duration = 200
                // icon.visibility = View.VISIBLE
            }
        }
    }

    private fun hideIconForAllTabs() {
        for (i in 0 until tabCount) {
            val icon = findViewByIdFromTab(getTabAt(i), R.id.image_view_category_icon)
            val textCategory = findViewByIdFromTab(getTabAt(i), R.id.text_view_category_title)
            if (icon is ImageView) {
                // icon.animate().scaleX(0.0f).scaleY(0.0f).duration = 200
                // icon.animate().alpha(0f).duration = 200
                icon.animate().translationY(-50f).duration = 200
                textCategory?.animate()?.translationY(-14.0f)?.duration = 200
                // icon.visibility = View.GONE
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

    class CategoriesItemTab(val title: String, val iconUrl: String, val inactiveIconUrl: String)
}