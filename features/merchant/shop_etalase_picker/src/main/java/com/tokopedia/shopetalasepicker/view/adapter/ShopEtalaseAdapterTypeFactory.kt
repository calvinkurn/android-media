package com.tokopedia.shopetalasepicker.view.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shopetalasepicker.view.adapter.viewholder.ShopEtalaseViewHolder
import com.tokopedia.shopetalasepicker.view.model.ShopEtalaseViewModel

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalaseAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(shopEtalaseViewModel: ShopEtalaseViewModel): Int {
        return ShopEtalaseViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ShopEtalaseViewHolder.LAYOUT) {
            ShopEtalaseViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}
