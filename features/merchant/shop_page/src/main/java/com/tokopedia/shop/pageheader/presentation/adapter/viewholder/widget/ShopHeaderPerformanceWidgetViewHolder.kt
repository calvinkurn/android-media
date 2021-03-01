package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration.HORIZONTAL
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.adapter.ShopHeaderPerformanceWidgetAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPerformanceWidgetAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

class ShopHeaderPerformanceWidgetViewHolder(
        itemView: View
) : AbstractViewHolder<ShopHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_performance_widget
    }

    private var shopPerformanceWidgetAdapter: ShopPerformanceWidgetAdapter? = null
    private var rvShopPerformanceWidget: RecyclerView? = itemView.findViewById(R.id.rv_shop_performance_widget)

    override fun bind(model: ShopHeaderWidgetUiModel) {
        shopPerformanceWidgetAdapter = ShopPerformanceWidgetAdapter(ShopHeaderPerformanceWidgetAdapterTypeFactory())
        rvShopPerformanceWidget?.apply {
            adapter = shopPerformanceWidgetAdapter
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerItemDecoration(itemView.context, HORIZONTAL))
        }
        shopPerformanceWidgetAdapter?.addComponents(model.components)
    }
}