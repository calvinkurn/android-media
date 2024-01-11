package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor as getCompatColor
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout.TabItemData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.home.R as homeR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MegaRecommendationTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TabLayout(context, attrs) {

    private var tabItemList = mutableListOf<TabItemData>()
    private var lastTabSelectedPosition = -1

    fun set(tabList: List<TabItemData>, pager: ViewPager?) {
        // initiate tab data sets
        tabItemList.clear()
        tabItemList.addAll(tabList)

        // disposable remaining state
        resetAllState()
        clearOnTabSelectedListeners()
        pager?.clearOnPageChangeListeners()

        // additional configuration
        isSmoothScrollingEnabled = true
        setupWithViewPager(pager)

        // setup view
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)?: continue
            tab.customView = createTabView(tab, i)
        }

        // listeners: TabLayout
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                updateTabIndicatorState()
            }

            override fun onTabReselected(tab: Tab?) = Unit
            override fun onTabUnselected(tab: Tab?) = Unit
        })

        // listeners: ViewPager
        pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                setActiveStateAt(position)
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(pos: Int, posOffset: Float, posOffsetPixels: Int) = Unit
        })

        // initial state
        setActiveStateAt(Int.ZERO)
    }

    private fun createTabView(tab: Tab, position: Int): View {
        val rootView = LayoutInflater.from(context).inflate(homeR.layout.tab_mega_recommendation, null)

        val title = rootView.findViewById<Typography>(homeR.id.txt_title)
        val shimmer = rootView.findViewById<LoaderUnify>(homeR.id.loader_shimmering)
        val image = rootView.findViewById<ImageView>(homeR.id.img_icon)

        if (tabItemList[position].title.isNotEmpty()) {
            title.text = tabItemList[position].title
            title.show()

            // inactive color state by default
            title.setTextColor(getCompatColor(context, INACTIVE_STATE_COLOR))
        } else {
            shimmer.show(); image.show()
            image.loadImageWithoutPlaceholder(tabItemList[position].imageUrl) {
                listener(onSuccess = { _, _ -> shimmer.hide() })
            }
        }

        rootView.setOnClickListener { selectTab(tab) }

        return rootView
    }

    private fun updateTabIndicatorState() {
        val currentSelectedPosition = selectedTabPosition
        if (lastTabSelectedPosition == currentSelectedPosition) return

        resetAllTabState()

        setInactiveStateAt(lastTabSelectedPosition)
        setActiveStateAt(currentSelectedPosition)

        lastTabSelectedPosition = currentSelectedPosition
    }

    private fun resetAllTabState() {
        for (i in 0 until tabCount) {
            setInactiveStateAt(i)
        }
    }

    private fun setActiveStateAt(position: Int) {
        updateStateAt(position) { title, indicator ->
            title.setTextColor(getCompatColor(context, ACTIVE_STATE_COLOR))
            indicator.show()
        }
    }

    private fun setInactiveStateAt(position: Int) {
        updateStateAt(position) { title, indicator ->
            title.setTextColor(getCompatColor(context, INACTIVE_STATE_COLOR))
            indicator.invisible()
        }
    }

    private fun updateStateAt(position: Int, state: (Typography, View) -> Unit) {
        val tab = getTabAt(position) ?: return

        val text = getTextFromTab(tab)
        val indicator = getIndicatorFromTab(tab)

        if (text != null && indicator != null) {
            state(text, indicator)
        }
    }

    private fun getTextFromTab(tab: Tab): Typography? {
        if (tab.customView == null) return null
        return tab.customView?.findViewById(homeR.id.txt_title)
    }

    private fun getIndicatorFromTab(tab: Tab): View? {
        if (tab.customView == null) return null
        return tab.customView?.findViewById(homeR.id.tab_indicator)
    }

    private fun resetAllState() {
        lastTabSelectedPosition = -1
    }

    companion object {
        private val ACTIVE_STATE_COLOR = unifyprinciplesR.color.Unify_GN500
        private val INACTIVE_STATE_COLOR = unifyprinciplesR.color.Unify_NN400
    }
}
