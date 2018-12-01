package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantNoteViewModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantNoteViewHolder(val view: View) : AbstractViewHolder<CheckoutVariantNoteViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_note_detail_product_page
    }

    override fun bind(element: CheckoutVariantNoteViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}