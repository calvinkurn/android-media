package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.ShopPageHorizontalItemDivider
import com.tokopedia.shop.databinding.LayoutShopHeaderPerformanceWidgetBinding
import com.tokopedia.shop.pageheader.presentation.adapter.ShopHeaderPerformanceWidgetAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPerformanceWidgetAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetImageOnlyComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetImageTextComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHeaderPerformanceWidgetViewHolder(
    itemView: View,
    private val shopPerformanceWidgetBadgeTextValueListener: ShopPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
    private val shopPerformanceWidgetImageOnlyListener: ShopPerformanceWidgetImageOnlyComponentViewHolder.Listener,
    private val shopPerformanceWidgetImageTextListener: ShopPerformanceWidgetImageTextComponentViewHolder.Listener
) : AbstractViewHolder<ShopHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_performance_widget
    }

    private val viewBinding: LayoutShopHeaderPerformanceWidgetBinding? by viewBinding()
    private var shopPerformanceWidgetAdapter: ShopPerformanceWidgetAdapter? = null
    private var rvShopPerformanceWidget: RecyclerView? = viewBinding?.rvShopPerformanceWidget

    override fun bind(model: ShopHeaderWidgetUiModel) {
        shopPerformanceWidgetAdapter = ShopPerformanceWidgetAdapter(
            ShopHeaderPerformanceWidgetAdapterTypeFactory(
                model,
                shopPerformanceWidgetBadgeTextValueListener,
                shopPerformanceWidgetImageOnlyListener,
                shopPerformanceWidgetImageTextListener
            )
        )
        rvShopPerformanceWidget?.apply {
            adapter = shopPerformanceWidgetAdapter
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            if (itemDecorationCount == 0) {
                val itemDecoration = ShopPageHorizontalItemDivider(
                    MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_performance_item_separator),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_1)
                )
                addItemDecoration(itemDecoration)
            }
        }
        shopPerformanceWidgetAdapter?.addComponents(model.components)
    }
}
