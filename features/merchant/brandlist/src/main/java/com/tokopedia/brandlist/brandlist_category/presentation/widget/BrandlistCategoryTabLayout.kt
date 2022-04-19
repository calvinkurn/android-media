package com.tokopedia.brandlist.brandlist_category.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.brandlist.R
import com.tokopedia.unifyprinciples.Typography

class BrandlistCategoryTabLayout(context: Context, attrs: AttributeSet?) : TabLayout(context, attrs) {

    private var tabMaxHeight: Int = 0
    private var tabMinHeight: Int = 0

    private val categoryTabModels = ArrayList<CategoryTabModel>()

    fun setup(viewPager: ViewPager?, tabModels: List<CategoryTabModel>, appBarLayout: AppBarLayout?) {

        setTabMaxHeight(getTabMaxHeightFromRes(resources))
        setTabMinHeight(getTabMinHeightFromRes(resources))

        // populate tab models
        populateBrandListTabModels(tabModels)

        // the tab layout needs to be setup first
        // before populated with contents
        setupWithViewPager(viewPager)

        // populate tabLayout.tab contents
        for (tabIndex in 0 until tabCount) {
            populateTabContent(getTabAt(tabIndex), tabModels[tabIndex])
        }

        // add on tab selected listener
        clearOnTabSelectedListeners()
        addOnTabSelectedListener(createOnTabSelectedListener(viewPager, categoryTabModels))

        // add on page change listener
        viewPager?.clearOnPageChangeListeners()
        viewPager?.addOnPageChangeListener(createOnPageChangeListener())
    }

    private fun getTabMaxHeightFromRes(resources: Resources): Int {
        return resources.getDimensionPixelSize(R.dimen.bl_tab_max_height)
    }

    private fun setTabMaxHeight(maxHeight: Int) {
        tabMaxHeight = maxHeight
    }

    private fun getTabMinHeightFromRes(resources: Resources): Int {
        return resources.getDimensionPixelSize(R.dimen.bl_tab_min_height)
    }

    private fun setTabMinHeight(minHeight: Int) {
        tabMinHeight = minHeight
    }

    private fun populateBrandListTabModels(categoryTabItems: List<CategoryTabModel>) {
        categoryTabModels.clear()
        categoryTabModels.addAll(categoryTabItems)
    }

    private fun populateTabContent(tab: Tab?, tabModel: CategoryTabModel) {
        tab?.customView = generateCustomView(context, tabModel)
    }

    @SuppressLint("InflateParams")
    private fun generateCustomView(context: Context, tabModel: CategoryTabModel): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_brand_list_category, null)
        val iconView: ImageView = view.findViewById(R.id.image_view_category_icon)
        val categoryTextView: Typography = view.findViewById(R.id.text_view_category_title)
        setTabIcon(iconView, tabModel.inactiveIconUrl)
        categoryTextView.text = tabModel.title
        return view
    }

    private fun createOnTabSelectedListener(viewPager: ViewPager?, categoryTabModels: ArrayList<CategoryTabModel>): OnTabSelectedListener {
        return object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab?) {
                tab?.let { activateTab(tab, categoryTabModels[tab.position]) }
            }

            override fun onTabUnselected(tab: Tab?) {
                tab?.let { deactivateTab(tab, categoryTabModels[tab.position]) }
            }

            override fun onTabSelected(tab: Tab?) {
                tab?.let { activateTab(tab, categoryTabModels[tab.position]) }
                tab?.let { viewPager?.currentItem = tab.position }
            }
        }
    }

    private fun activateTab(tab: Tab?, tabModel: CategoryTabModel) {
        val iconView: ImageView? = tab?.customView?.findViewById(R.id.image_view_category_icon)
        iconView?.let { setTabIcon(it, tabModel.iconUrl) }
        val categoryTextView: Typography? = tab?.customView?.findViewById(R.id.text_view_category_title)
        categoryTextView?.let { setActiveText(categoryTextView) }
    }

    private fun deactivateTab(tab: Tab?, tabModel: CategoryTabModel) {
        val iconView: ImageView? = tab?.customView?.findViewById(R.id.image_view_category_icon)
        iconView?.let { setTabIcon(it, tabModel.inactiveIconUrl) }
        val categoryTextView: Typography? = tab?.customView?.findViewById(R.id.text_view_category_title)
        categoryTextView?.let { setInactiveText(categoryTextView) }
    }

    private fun setTabIcon(iconView: ImageView, iconUrl: String) {
        ImageHandler.loadImage(context, iconView, iconUrl, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
    }

    private fun setActiveText(categoryView: Typography) {
        categoryView.apply {
            setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_P600))
            setWeight(Typography.BOLD)
        }
    }

    private fun setInactiveText(categoryView: Typography) {
        categoryView.apply {
            setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            setWeight(Typography.REGULAR)
        }
    }

    private fun createOnPageChangeListener(): ViewPager.OnPageChangeListener {
        return object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                getTabAt(position)?.select()
            }
        }
    }

    class CategoryTabModel(val title: String, val iconUrl: String, val inactiveIconUrl: String)
}
