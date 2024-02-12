package com.tokopedia.home_component.widget.tab

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
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
 * <com.tokopedia.home_component.widget.tab.MegaTabLayout
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
class MegaTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TabLayout(context, attrs) {

    private var tabItemList = mutableListOf<MegaTabItem>()
    private var lastTabSelectedPosition = -1

    private var useInactiveImageState = false

    init {
        initAttrs(attrs)

        setSelectedTabIndicator(null)
        tabMode = MODE_SCROLLABLE
        tabRippleColor = null
    }

    private fun initAttrs(attrSet: AttributeSet?) {
        if (attrSet == null) return

        val attrList = context.obtainStyledAttributes(attrSet, R.styleable.MegaTabLayout)
        val shouldUseInactiveImageMode = attrList.getBoolean(
            R.styleable.MegaTabLayout_grayscale_inactive_mode,
            false
        )

        useInactiveImageState = shouldUseInactiveImageMode
        attrList.recycle()
    }

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
                setInactiveStateAt(position)
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
        val rootView = MegaTabView(context)

        rootView.setOnClickListener { selectTab(tab) }
        rootView.createTabView(item)

        if (position < tabCount - 1) {
            val paddingEnd = resources.getDimensionPixelOffset(R.dimen.home_recommendation_mega_tab_padding)
            rootView.setPadding(rootView.paddingLeft, rootView.paddingTop, paddingEnd, rootView.paddingBottom)
        }

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
        updateStateAt(position) { view ->
            view.typography?.setTextColor(ContextCompat.getColor(context, ACTIVE_STATE_COLOR))
            view.indicator?.show()
            updateImageState(view.imageView, true)
        }
    }

    private fun setInactiveStateAt(position: Int) {
        updateStateAt(position) { view ->
            view.typography?.setTextColor(ContextCompat.getColor(context, INACTIVE_STATE_COLOR))
            view.indicator?.invisible()
            updateImageState(view.imageView, false)
        }
    }

    private fun updateImageState(imageView: ImageView?, isActiveState: Boolean) {
        if (useInactiveImageState && imageView?.isVisible == true) {
            if (isActiveState) {
                imageView.colorFilter = null
            } else {
                val colorMatrix =  ColorMatrix()
                colorMatrix.setSaturation(0.0f)
                val filter =  ColorMatrixColorFilter(colorMatrix)
                imageView.colorFilter = filter
            }
        }
    }

    private fun updateStateAt(position: Int, state: (TabStateView) -> Unit) {
        val tab = getTabAt(position) ?: return
        state(TabStateView.get(tab))
    }

    private fun resetAllState() {
        lastTabSelectedPosition = -1
    }

    /**
     * An view comes from [MegaTabView].
     *
     * This [TabStateView] tries to extract the views from [MegaTabView] were added into this [MegaTabLayout].
     * The main goal is, we've ability to access the views make some modification of it, such as visibility,
     * change the background color, etc.
     */
    internal data class TabStateView(
        val typography: Typography?,
        val indicator: View?,
        val imageView: ImageView?,
    ) {

        companion object {
            fun get(tab: Tab): TabStateView {
                return TabStateView(
                    typography = tab.customView?.findViewById(R.id.txt_title),
                    indicator = tab.customView?.findViewById(R.id.tab_indicator),
                    imageView = tab.customView?.findViewById(R.id.img_icon),
                )
            }
        }
    }

    companion object {
        private val ACTIVE_STATE_COLOR = unifyprinciplesR.color.Unify_GN500
        private val INACTIVE_STATE_COLOR = unifyprinciplesR.color.Unify_NN950
    }
}
