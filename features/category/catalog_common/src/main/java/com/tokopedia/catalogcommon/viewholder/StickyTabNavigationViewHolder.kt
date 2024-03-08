package com.tokopedia.catalogcommon.viewholder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetStickyNavigationBinding
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class StickyTabNavigationViewHolder(
    itemView: View,
    val listener: StickyNavigationListener? = null
) :
    AbstractViewHolder<StickyNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_sticky_navigation
    }

    private val binding by viewBinding<WidgetStickyNavigationBinding>()

    override fun bind(element: StickyNavigationUiModel?) {
        binding?.let {
            it.catalogTabsUnify.removeAllTabs()
            it.catalogTabsUnify.clearOnTabSelectedListeners()
            setupTabs(
                element,
                element?.widgetBackgroundColor ?: Color.TRANSPARENT
            )
        }
    }

    private fun setupTabs(
        element: StickyNavigationUiModel?,
        widgetBackgroundColor: Int
    ) {
        binding?.run {
            if (catalogTabsUnify.tabCount == Int.ZERO) {
                element?.content?.forEachIndexed { index, item ->
                    val customText = LayoutInflater.from(itemView.context)
                        .inflate(R.layout.item_tab_menu, null) as LinearLayout
                    val textView = customText.findViewById<Typography>(R.id.tvText)
                    textView.text = item.title
                    textView.setLineSpacing(4f, 1f)
                    catalogTabsUnify.addTab(catalogTabsUnify.newTab().setCustomView(customText))
                }
            }
            catalogTabsUnify.getTabAt(element?.currentSelectTab.orZero())?.select()

            catalogTabsUnify.tabMode = TabLayout.MODE_FIXED
            catalogTabsUnify.isTabIndicatorFullWidth = false
            catalogTabsUnify.tabRippleColor = null
            catalogTabsUnify.setBackgroundColor(widgetBackgroundColor)
            val centeredTabIndicator = ContextCompat.getDrawable(
                catalogTabsUnify.context,
                R.drawable.shape_showcase_tab_indicator_color
            )
            catalogTabsUnify.setSelectedTabIndicatorColor(ContextCompat.getColor(catalogTabsUnify.context, unifyprinciplesR.color.Unify_GN500))
            catalogTabsUnify.setSelectedTabIndicator(centeredTabIndicator)
            catalogTabsUnify.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    listener?.onNavigateWidget(
                        element?.content?.getOrNull(tab?.position.orZero())?.anchorTo.orEmpty(),
                        tab?.position.orZero(),
                        element?.content?.getOrNull(tab?.position.orZero())?.title.orEmpty()
                    )
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            listener?.onStickyNavigationImpression()
        }
    }
}

interface StickyNavigationListener {

    fun onNavigateWidget(anchorTo: String, tabPosition: Int, tabTitle: String?)

    fun onStickyNavigationImpression()
}
