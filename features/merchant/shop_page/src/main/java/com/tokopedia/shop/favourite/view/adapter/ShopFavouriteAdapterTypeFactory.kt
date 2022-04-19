package com.tokopedia.shop.favourite.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.shop.favourite.view.adapter.viewholder.ShopFavouriteViewHolder
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel

/**
 * Created by alvarisi on 12/7/17.
 */
class ShopFavouriteAdapterTypeFactory(private val callback: BaseEmptyViewHolder.Callback) : BaseAdapterTypeFactory() {
    fun type(shopFollowerUiModel: ShopFollowerUiModel?): Int {
        return ShopFavouriteViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        viewHolder = if (viewType == ShopFavouriteViewHolder.LAYOUT) {
            ShopFavouriteViewHolder(view)
        } else if (viewType == EmptyViewHolder.LAYOUT) {
            EmptyViewHolder(view, callback)
        } else {
            super.createViewHolder(view, viewType)
        }
        return viewHolder
    }
}