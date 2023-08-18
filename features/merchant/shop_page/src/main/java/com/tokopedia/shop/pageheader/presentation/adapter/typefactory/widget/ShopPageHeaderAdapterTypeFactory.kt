package com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.*
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderPerformanceWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderActionButtonWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderBasicInfoWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_PERFORMANCE
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_PLAY

class ShopPageHeaderAdapterTypeFactory(
    private val shopPageHeaderBasicInfoWidgetListener: ShopPageHeaderBasicInfoWidgetViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetBadgeTextValueListener: ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetImageOnlyListener: ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetChatButtonComponentListener: ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetFollowButtonComponentListener: ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder.Listener,
    private val shopPageHeaderActionButtonWidgetNoteButtonComponentListener: ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder.Listener,
    private val sgcPlayWidget: ShopPageTrackingSGCPlayWidget?,
    private val shopPagePlayWidgetListener: ShopPageHeaderPlayWidgetViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetImageTextListener: ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder.Listener,
) : BaseAdapterTypeFactory() {

    private var adapterShopHeader: ShopPageHeaderAdapter? = null

    fun attachAdapter(adapter: ShopPageHeaderAdapter) {
        adapterShopHeader = adapter
    }

    fun type(modelPage: ShopPageHeaderWidgetUiModel): Int {
        return when (modelPage.type.toLowerCase()) {
            SHOP_BASIC_INFO.toLowerCase() -> ShopPageHeaderBasicInfoWidgetViewHolder.LAYOUT
            SHOP_PERFORMANCE.toLowerCase() -> ShopHeaderPerformanceWidgetViewHolder.LAYOUT
            SHOP_ACTION.toLowerCase() -> ShopPageHeaderActionButtonWidgetViewHolder.LAYOUT
            SHOP_PLAY.toLowerCase() -> ShopPageHeaderPlayWidgetViewHolder.LAYOUT
            else -> HideViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopPageHeaderBasicInfoWidgetViewHolder.LAYOUT -> ShopPageHeaderBasicInfoWidgetViewHolder(
                parent,
                shopPageHeaderBasicInfoWidgetListener
            )
            ShopHeaderPerformanceWidgetViewHolder.LAYOUT -> ShopHeaderPerformanceWidgetViewHolder(
                parent,
                shopPageHeaderPerformanceWidgetBadgeTextValueListener,
                shopPageHeaderPerformanceWidgetImageOnlyListener,
                shopPageHeaderPerformanceWidgetImageTextListener
            )
            ShopPageHeaderActionButtonWidgetViewHolder.LAYOUT -> ShopPageHeaderActionButtonWidgetViewHolder(
                parent,
                shopPageHeaderActionButtonWidgetChatButtonComponentListener,
                shopPageHeaderActionButtonWidgetFollowButtonComponentListener,
                shopPageHeaderActionButtonWidgetNoteButtonComponentListener,
                adapterShopHeader
            )
            ShopPageHeaderPlayWidgetViewHolder.LAYOUT -> ShopPageHeaderPlayWidgetViewHolder(
                parent,
                sgcPlayWidget,
                shopPagePlayWidgetListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
