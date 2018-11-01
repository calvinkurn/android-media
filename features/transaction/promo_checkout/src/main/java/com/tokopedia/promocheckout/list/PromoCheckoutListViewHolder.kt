package com.tokopedia.promocheckout.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import kotlinx.android.synthetic.main.item_list_promo_checkout.view.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.view.*

class PromoCheckoutListViewHolder(val view: View?) : AbstractViewHolder<PromoCheckoutListModel>(view) {

    override fun bind(element: PromoCheckoutListModel?) {
        ImageHandler.loadImageRounded2(view?.context, view?.imageBannerPromo, element?.imageUrlMobile)
        view?.textMinTrans?.text = element?.catalogTitle
    }

    companion object {
        val LAYOUT = R.layout.item_list_promo_checkout
    }
}
