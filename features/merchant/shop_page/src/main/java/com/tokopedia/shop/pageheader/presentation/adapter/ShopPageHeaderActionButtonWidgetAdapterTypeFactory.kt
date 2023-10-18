package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopPageHeaderButtonComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_CHAT
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_SHOP_NOTES
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel

class ShopPageHeaderActionButtonWidgetAdapterTypeFactory(
    private val shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
    private val shopPageHeaderActionButtonWidgetChatButtonComponentListener: ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetFollowButtonComponentListener: ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetNoteButtonComponentListener: ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder.Listener
) : BaseAdapterTypeFactory(), ShopPageHeaderButtonComponentTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder.LAYOUT -> ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder(
                parent,
                shopPageHeaderActionButtonWidgetNoteButtonComponentListener
            )
            ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder.LAYOUT -> ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder(
                parent,
                shopPageHeaderWidgetUiModel,
                shopPageHeaderActionButtonWidgetChatButtonComponentListener
            )
            ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder.LAYOUT -> ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder(
                parent,
                shopPageHeaderWidgetUiModel,
                shopPageHeaderActionButtonWidgetFollowButtonComponentListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(model: ShopPageHeaderButtonComponentUiModel): Int {
        return when (model.name) {
            BUTTON_SHOP_NOTES -> ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder.LAYOUT
            BUTTON_CHAT -> ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder.LAYOUT
            BUTTON_FOLLOW -> ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder.LAYOUT
            else -> HideViewHolder.LAYOUT
        }
    }
}
