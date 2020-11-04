package com.tokopedia.shopwidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class ShopCardAdapterTypeFactory(private val shopWidgetListener: ShopWidgetListener) : BaseAdapterTypeFactory(), ShopCardTypeFactory {

    override fun type(type: String): Int {
        return when(type) {
            SUGGESTION_TOP_SHOP -> ShopWidgetCardViewHolder.LAYOUT
            SUGGESTION_TOP_SHOP_SEE_MORE -> ShopWidgetSeeMoreCardViewHolder.LAYOUT
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ShopWidgetCardViewHolder.LAYOUT -> ShopWidgetCardViewHolder(parent, shopWidgetListener)
            ShopWidgetSeeMoreCardViewHolder.LAYOUT -> ShopWidgetSeeMoreCardViewHolder(parent, shopWidgetListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}