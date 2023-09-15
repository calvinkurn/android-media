package com.tokopedia.catalogcommon.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetStickyNavigationBinding
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.utils.view.binding.viewBinding

class StickyTabNavigationViewHolder(itemView: View, val listener: StickyNavigationListener?) :
    AbstractViewHolder<StickyNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_sticky_navigation
    }

    private val binding by viewBinding<WidgetStickyNavigationBinding>()


    override fun bind(element: StickyNavigationUiModel?) {
        binding?.let {
            it.catalogTabsUnify.tabLayout.removeAllTabs()
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
            element?.content?.forEach {
                catalogTabsUnify.addNewTab(it.title)
            }
            catalogTabsUnify.customTabMode = TabLayout.MODE_FIXED
            catalogTabsUnify.tabLayout.isTabIndicatorFullWidth = false
            catalogTabsUnify.tabLayout.setBackgroundColor(widgetBackgroundColor)
            val centeredTabIndicator = ContextCompat.getDrawable(
                catalogTabsUnify.tabLayout.context,
                R.drawable.shape_showcase_tab_indicator_color
            )
            catalogTabsUnify.tabLayout.getTabAt(element?.currentSelectTab.orZero())?.select()
            catalogTabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)
            catalogTabsUnify.tabLayout.setTabTextColors(Color.BLUE, Color.BLACK)
            catalogTabsUnify.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    element?.currentSelectTab = tab?.position.orZero()
                    listener?.onNavigateWidget(element?.content?.get(tab?.position.orZero())?.anchorTo.orEmpty(), element?.currentSelectTab.orZero())
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    element?.currentSelectTab = tab?.position.orZero()
                    listener?.onNavigateWidget(element?.content?.get(tab?.position.orZero())?.anchorTo.orEmpty(), element?.currentSelectTab.orZero())
                }

            })

        }
    }

}

interface StickyNavigationListener {

    fun onNavigateWidget(anchorTo: String, tabPosition: Int)
}
