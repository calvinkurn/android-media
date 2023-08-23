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
            setupTabs(listOf())
        }
    }

    private fun setupTabs(tabs: List<StickyNavigationUiModel.StickyNavigationItemData>) {
        binding?.run {
//            tabs.forEach {
//                catalogTabsUnify.addNewTab(it.title)
//            }
//            catalogTabsUnify.tabLayout.isTabIndicatorFullWidth = false
//            catalogTabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
//            val centeredTabIndicator = ContextCompat.getDrawable(
//                catalogTabsUnify.tabLayout.context,
//                R.drawable.shape_showcase_tab_indicator_color
//            )
//            catalogTabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)
        }
    }


}
