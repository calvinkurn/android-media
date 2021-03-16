package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderButtonComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_CHAT
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_SHOP_NOTES
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

class ShopHeaderActionButtonWidgetAdapterTypeFactory(
        private val shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
        private val shopActionButtonWidgetChatButtonComponentListener: ShopActionButtonWidgetChatButtonComponentViewHolder.Listener,
        private val shopActionButtonWidgetFollowButtonComponentListener: ShopActionButtonWidgetFollowButtonComponentViewHolder.Listener
) : BaseAdapterTypeFactory(), ShopHeaderButtonComponentTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopActionButtonWidgetNoteButtonComponentViewHolder.LAYOUT -> ShopActionButtonWidgetNoteButtonComponentViewHolder(
                    parent
            )
            ShopActionButtonWidgetChatButtonComponentViewHolder.LAYOUT -> ShopActionButtonWidgetChatButtonComponentViewHolder(
                    parent,
                    shopHeaderWidgetUiModel,
                    shopActionButtonWidgetChatButtonComponentListener
            )
            ShopActionButtonWidgetFollowButtonComponentViewHolder.LAYOUT -> ShopActionButtonWidgetFollowButtonComponentViewHolder(
                    parent,
                    shopHeaderWidgetUiModel,
                    shopActionButtonWidgetFollowButtonComponentListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(model: ShopHeaderButtonComponentUiModel): Int {
        return when (model.name) {
            BUTTON_SHOP_NOTES -> ShopActionButtonWidgetNoteButtonComponentViewHolder.LAYOUT
            BUTTON_CHAT -> ShopActionButtonWidgetChatButtonComponentViewHolder.LAYOUT
            BUTTON_FOLLOW -> ShopActionButtonWidgetFollowButtonComponentViewHolder.LAYOUT
            else -> HideViewHolder.LAYOUT
        }
    }
}