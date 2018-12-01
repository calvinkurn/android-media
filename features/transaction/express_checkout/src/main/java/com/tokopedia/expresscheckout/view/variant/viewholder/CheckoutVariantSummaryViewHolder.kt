package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantSummaryViewModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantSummaryViewHolder(val view: View) : AbstractViewHolder<CheckoutVariantSummaryViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_summary_detail_product_page
    }

    override fun bind(element: CheckoutVariantSummaryViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}