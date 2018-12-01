package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantProfileViewModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantProfileViewHolder(val view: View) : AbstractViewHolder<CheckoutVariantProfileViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_profile_detail_product_page
    }

    override fun bind(element: CheckoutVariantProfileViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}