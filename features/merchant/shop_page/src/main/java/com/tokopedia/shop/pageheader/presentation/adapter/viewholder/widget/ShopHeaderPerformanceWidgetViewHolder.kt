package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.ShopPageHorizontalItemDivider
import com.tokopedia.shop.databinding.LayoutShopHeaderPerformanceWidgetBinding
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderPerformanceWidgetAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderPerformanceWidgetAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHeaderPerformanceWidgetViewHolder(
    itemView: View,
    private val shopPageHeaderPerformanceWidgetBadgeTextValueListener: ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetImageOnlyListener: ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetImageTextListener: ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder.Listener
) : AbstractViewHolder<ShopPageHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_performance_widget
    }

    private val viewBinding: LayoutShopHeaderPerformanceWidgetBinding? by viewBinding()
    private var shopPageHeaderPerformanceWidgetAdapter: ShopPageHeaderPerformanceWidgetAdapter? = null
    private var rvShopPerformanceWidget: RecyclerView? = viewBinding?.rvShopPerformanceWidget

    override fun bind(modelPage: ShopPageHeaderWidgetUiModel) {
        shopPageHeaderPerformanceWidgetAdapter = ShopPageHeaderPerformanceWidgetAdapter(
            ShopPageHeaderPerformanceWidgetAdapterTypeFactory(
                modelPage,
                shopPageHeaderPerformanceWidgetBadgeTextValueListener,
                shopPageHeaderPerformanceWidgetImageOnlyListener,
                shopPageHeaderPerformanceWidgetImageTextListener
            )
        )
        rvShopPerformanceWidget?.apply {
            adapter = shopPageHeaderPerformanceWidgetAdapter
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            if (itemDecorationCount == 0) {
                val itemDecoration = ShopPageHorizontalItemDivider(
                    MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_performance_item_separator),
                    itemView.context.resources.getDimensionPixelSize(com.tokopedia.shop.R.dimen.dp_1)
                )
                addItemDecoration(itemDecoration)
            }
        }
        shopPageHeaderPerformanceWidgetAdapter?.addComponents(modelPage.componentPages)
    }
}
