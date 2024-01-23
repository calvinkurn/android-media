package com.tokopedia.home_component.widget.tab

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.tabs.TabLayout
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * A shared mega-tab component.
 *
 * MegaTab is a categorical tab with multiple tab view support (text and image).
 *
 * We aim to ensure that each megatab contents clearly communicates the distinct benefits that buyers
 * can expect when exploring each tab, such as 'paling diskon,' 'terdekat denganmu,' 'brand favorit,'
 * and 'mirip yang kamu cek' with no repeated content across tabs.
 *
 * Please read this technical usage for more detail below.
 *
 * Sample Usage:
 * <com.tokopedia.home_component.widget.tab.RecommendationMegaTabLayout
 *    android:id="@+id/tab_recommendation"
 *    android:layout_width="match_parent"
 *    android:layout_height="wrap_content"
 *    android:paddingStart="16dp"
 *    android:paddingEnd="16dp"
 *    android:paddingTop="8dp"
 *    android:clipToPadding="false"
 *    app:tabIndicatorHeight="0dp"
 *    app:tabMode="scrollable"
 *    app:tabPaddingEnd="0dp"
 *    app:tabPaddingStart="0dp"
 *    app:tabPaddingBottom="0dp"
 *    app:tabRippleColor="@null"/>
 *
 * Apply the tab data set by using [MegaTabItem] data class.
 *
 * import com.tokopedia.home_component.widget.tab.RecommendationMegaTabLayout.Item as MegaTabItem
 * ...
 *
 * val tabs = listOf(
 *     MegaTabItem(title = "For You"),
 *     MegaTabItem(imageUrl = "http://images.tokopedia.net/sample.png"),
 *     MegaTabItem(title = "Paling Diskon")
 * )
 *
 * // setup view
 * tabRecommendation?.set(tab)
 *
 * // or with view-page nature behavior
 * tabRecommendation?.set(tab, viewPager)
 *
 * Notes:
 * 1. Due we're trying to customize the TabLayout, you have to set a few wdiget attributes to disable
 *    certain pre-defined behavior from TabLayout. As we mentioned above, you have to at least disable
 *    these attributes:
 *    - tabIndicatorHeight
 *    - tabPadding***
 *    - tabRippleColor
 *
 * 2. This component is ala carte which there's no pre-define inner-space (i.e. padding) implemented.
 *    If you want to set the padding for particular area, you could do it with a common way by using
 *    padding***. Also, don't forget to disable the clipToPadding as well.
 *
 * @author: Home
 * @since: January, 2024
 */
class RecommendationMegaTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TabLayout(context, attrs) {

    private var tabItemList = mutableListOf<MegaTabItem>()
    private var lastTabSelectedPosition = -1

    fun set(tabList: List<MegaTabItem>, pager: ViewPager? = null) {
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
        for (position in 0 until tabCount) {
            val tab = getTabAt(position)?: continue

            if (tab.customView == null) {
                val item = tabItemList[position]
                if (item.hasContentEmpty()) continue

                tab.customView = createTabView(tab, item, position)
            }
        }

        // listener: TabLayout
        onTabSelected { updateTabIndicatorState() }

        // listener: ViewPager
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

    private fun createTabView(tab: Tab, item: MegaTabItem, position: Int): View {
        val rootView = LayoutInflater.from(context).inflate(R.layout.tab_mega_recommendation, null)

        val title = rootView.findViewById<Typography>(R.id.txt_title)
        val shimmer = rootView.findViewById<LoaderUnify>(R.id.loader_shimmering)
        val image = rootView.findViewById<ImageView>(R.id.img_icon)

        // set tab title
        title.text = item.title

        // inactive color state by default
        title.setTextColor(ContextCompat.getColor(context, INACTIVE_STATE_COLOR))

        if (position < tabCount - 1) {
            val paddingEnd = resources.getDimensionPixelOffset(R.dimen.home_recommendation_mega_tab_padding)
            rootView.setPadding(rootView.paddingLeft, rootView.paddingTop, paddingEnd, rootView.paddingBottom)
        }

        if (item.imageUrl.isNotEmpty()) {
            shimmer.show(); image.show()
            image.loadImageWithoutPlaceholder(item.imageUrl) {
                setSignatureKey(ObjectKey(System.currentTimeMillis())) // temporary for debug
                listener(
                    onSuccess = { _, _ -> shimmer.hide() },
                    onError = { _ ->
                        image.hide()
                        title.show()
                    }
                )
            }
        } else {
            title.show()
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
            title.setTextColor(ContextCompat.getColor(context, ACTIVE_STATE_COLOR))
            indicator.show()
        }
    }

    private fun setInactiveStateAt(position: Int) {
        updateStateAt(position) { title, indicator ->
            title.setTextColor(ContextCompat.getColor(context, INACTIVE_STATE_COLOR))
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
        return tab.customView?.findViewById(R.id.txt_title)
    }

    private fun getIndicatorFromTab(tab: Tab): View? {
        if (tab.customView == null) return null
        return tab.customView?.findViewById(R.id.tab_indicator)
    }

    private fun resetAllState() {
        lastTabSelectedPosition = -1
    }

    companion object {
        private val ACTIVE_STATE_COLOR = unifyprinciplesR.color.Unify_GN500
        private val INACTIVE_STATE_COLOR = unifyprinciplesR.color.Unify_NN400
    }
}
