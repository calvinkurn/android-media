package com.tokopedia.promocheckout.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel

class PromoCheckoutListAdapterFactory(val listenerTrackingCoupon: PromoCheckoutListViewHolder.ListenerTrackingCoupon) : BaseAdapterTypeFactory() {

    fun type(promoCheckoutListModel: PromoCheckoutListModel) : Int {
        return PromoCheckoutListViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return PromoCheckoutListEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if(type == PromoCheckoutListViewHolder.LAYOUT){
            return PromoCheckoutListViewHolder(parent, listenerTrackingCoupon)
        } else if (type == PromoCheckoutListEmptyViewHolder.LAYOUT) {
            return PromoCheckoutListEmptyViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }
}
