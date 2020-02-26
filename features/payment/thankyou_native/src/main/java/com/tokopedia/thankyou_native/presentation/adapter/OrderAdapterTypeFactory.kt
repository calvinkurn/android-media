package com.tokopedia.thankyou_native.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.model.PurchaseItemAdapterModel
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopItemAdapterModel
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.PurchaseItemViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.ShopNameViewHolder

class OrderAdapterTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when(type){
            ShopNameViewHolder.LAYOUT_ID -> ShopNameViewHolder(parent!!)
            PurchaseItemViewHolder.LAYOUT_ID -> PurchaseItemViewHolder(parent!!)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(purchaseItemAdapterModel: PurchaseItemAdapterModel): Int {
        return PurchaseItemViewHolder.LAYOUT_ID
    }

    fun type(shopItemAdapterModel: ShopItemAdapterModel): Int? {
        return ShopNameViewHolder.LAYOUT_ID
    }

}