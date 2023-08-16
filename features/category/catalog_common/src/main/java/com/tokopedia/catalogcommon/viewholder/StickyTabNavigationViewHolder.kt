package com.tokopedia.catalogcommon.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetStickyNavigationBinding
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class StickyTabNavigationViewHolder(itemView: View): AbstractViewHolder<StickyNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_sticky_navigation
    }

    private val binding by viewBinding<WidgetStickyNavigationBinding>()

    override fun bind(element: StickyNavigationUiModel?) {
        binding?.let {
            setupTabs()
        }
    }

    private fun setupTabs(tabs: List<StickyNavigationUiModel.StickyNavigationItemData>) {
        binding?.run {
            tabsUnify.tabLayout.isTabIndicatorFullWidth = false
            tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
            val centeredTabIndicator = ContextCompat.getDrawable(
                tabsUnify.tabLayout.context,
                R.drawable.shape_showcase_tab_indicator_color
            )
            tabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)
            tabsUnify.addNewTab("test 1")
            tabsUnify.addNewTab("test 2")
            tabsUnify.addNewTab("test 3")
            tabsUnify.addNewTab("test 4")
            tabsUnify.addNewTab("test 5")
            tabsUnify.addNewTab("test 6")
        }
    }


}
