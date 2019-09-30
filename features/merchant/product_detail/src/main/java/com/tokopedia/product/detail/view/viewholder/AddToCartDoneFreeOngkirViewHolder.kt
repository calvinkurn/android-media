package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneFreeOngkirTitle
import kotlinx.android.synthetic.main.add_to_cart_done_free_ongkir_layout.view.*

class AddToCartDoneFreeOngkirViewHolder(itemView: View) : AbstractViewHolder<AddToCartDoneFreeOngkirTitle>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_free_ongkir_layout
    }

    override fun bind(element: AddToCartDoneFreeOngkirTitle) {
        with(itemView) {
            free_ongkir_txt.text = MethodChecker.fromHtml(element.freeOngkirTitle)
        }
    }
}