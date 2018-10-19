package com.tokopedia.promocheckout.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.PromoCheckoutListModel

class PromoCheckoutListViewHolder(view: View?) : AbstractViewHolder<PromoCheckoutListModel>(view) {

    override fun bind(element: PromoCheckoutListModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val LAYOUT = R.layout.item_list_promo_checkout
    }
}
