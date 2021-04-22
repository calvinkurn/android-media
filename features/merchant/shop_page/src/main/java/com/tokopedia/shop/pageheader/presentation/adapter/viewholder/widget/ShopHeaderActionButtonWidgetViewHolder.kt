package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import androidx.recyclerview.widget.*
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.ShopPageActionButtonWidgetMarginItemDivider
import com.tokopedia.shop.pageheader.presentation.adapter.ShopActionButtonWidgetAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.ShopHeaderActionButtonWidgetAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopActionButtonWidgetChatButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopActionButtonWidgetFollowButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

class ShopHeaderActionButtonWidgetViewHolder(
        itemView: View,
        private val shopActionButtonWidgetChatButtonComponentListener: ShopActionButtonWidgetChatButtonComponentViewHolder.Listener,
        private val shopActionButtonWidgetFollowButtonComponentListener: ShopActionButtonWidgetFollowButtonComponentViewHolder.Listener,
        private val adapterShopHeader: ShopPageHeaderAdapter?
) : AbstractViewHolder<ShopHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_button_widget
    }

    private val recyclerViewButtonComponent: RecyclerView? = itemView.findViewById(R.id.rv_button_component)

    override fun bind(model: ShopHeaderWidgetUiModel) {
        val shopActionButtonWidgetAdapter = ShopActionButtonWidgetAdapter(ShopHeaderActionButtonWidgetAdapterTypeFactory(
                model,
                shopActionButtonWidgetChatButtonComponentListener,
                shopActionButtonWidgetFollowButtonComponentListener
        ))
        adapterShopHeader?.setAdapterWidgetButton(shopActionButtonWidgetAdapter)
        recyclerViewButtonComponent?.apply {
            adapter = shopActionButtonWidgetAdapter
            val manager= FlexboxLayoutManager(itemView.context)
            layoutManager = manager
            if (itemDecorationCount == 0)
                addItemDecoration(ShopPageActionButtonWidgetMarginItemDivider())
        }
        shopActionButtonWidgetAdapter?.addComponents(model.components)
    }
}