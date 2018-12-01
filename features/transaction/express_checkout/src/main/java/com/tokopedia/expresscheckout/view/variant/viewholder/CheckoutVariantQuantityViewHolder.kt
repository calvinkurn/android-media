package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantQuantityViewModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantQuantityViewHolder(val view: View) : AbstractViewHolder<CheckoutVariantQuantityViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_quantity_detail_product_page
    }

    override fun bind(element: CheckoutVariantQuantityViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}