package com.tokopedia.shop.settings.address.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import com.tokopedia.shop.settings.address.view.viewholder.ShopLocationViewHolder

class ShopLocationTypeFactory(private val listener: ShopLocationViewHolder.OnIconMoreClicked): BaseAdapterTypeFactory(){

    fun type(shopLocation: ShopLocationViewModel) = ShopLocationViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            ShopLocationViewHolder.LAYOUT -> ShopLocationViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}