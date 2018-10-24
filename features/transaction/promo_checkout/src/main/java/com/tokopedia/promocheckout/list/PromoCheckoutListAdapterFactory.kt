package com.tokopedia.promocheckout.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel

class PromoCheckoutListAdapterFactory : BaseAdapterTypeFactory() {

    fun type(promoCheckoutListModel: PromoCheckoutListModel) : Int {
        return PromoCheckoutListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if(type == PromoCheckoutListViewHolder.LAYOUT){
            return PromoCheckoutListViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }
}
