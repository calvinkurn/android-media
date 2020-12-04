package com.tokopedia.manageaddress.ui.shoplocation.shopaddress.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.viewholder.ShopLocationViewHolder

class ShopLocationOldTypeFactory(private val listener: ShopLocationViewHolder.OnIconMoreClicked): BaseAdapterTypeFactory(){

    fun type(shopLocationOld: ShopLocationOldUiModel) = ShopLocationViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            ShopLocationViewHolder.LAYOUT -> ShopLocationViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}