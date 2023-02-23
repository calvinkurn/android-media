package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import androidx.recyclerview.widget.*
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopHeaderButtonWidgetBinding
import com.tokopedia.shop.pageheader.presentation.ShopPageActionButtonWidgetMarginItemDivider
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderActionButtonWidgetAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderActionButtonWidgetAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderActionButtonWidgetViewHolder(
    itemView: View,
    private val shopPageHeaderActionButtonWidgetChatButtonComponentListener: ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetFollowButtonComponentListener: ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetNoteButtonComponentListener: ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder.Listener,
    private val adapterShopHeader: ShopPageHeaderAdapter?
) : AbstractViewHolder<ShopHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_button_widget
    }

    private val viewBinding: LayoutShopHeaderButtonWidgetBinding? by viewBinding()
    private val recyclerViewButtonComponent: RecyclerView? = viewBinding?.rvButtonComponent

    override fun bind(model: ShopHeaderWidgetUiModel) {
        val shopActionButtonWidgetAdapter = ShopPageHeaderActionButtonWidgetAdapter(
            ShopPageHeaderActionButtonWidgetAdapterTypeFactory(
                model,
                shopPageHeaderActionButtonWidgetChatButtonComponentListener,
                shopPageHeaderActionButtonWidgetFollowButtonComponentListener,
                shopPageHeaderActionButtonWidgetNoteButtonComponentListener
            )
        )
        adapterShopHeader?.setAdapterWidgetButton(shopActionButtonWidgetAdapter)
        recyclerViewButtonComponent?.apply {
            adapter = shopActionButtonWidgetAdapter
            val manager = FlexboxLayoutManager(itemView.context)
            layoutManager = manager
            if (itemDecorationCount == 0) {
                addItemDecoration(ShopPageActionButtonWidgetMarginItemDivider())
            }
        }
        shopActionButtonWidgetAdapter?.addComponents(model.components)
    }
}
