package com.tokopedia.shop.open.view.holder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.data.model.PostalCodeViewModel
import kotlinx.android.synthetic.main.listview_postal_code.view.*

class PostalCodeChooserViewHolder(itemView: View) : AbstractViewHolder<PostalCodeViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.listview_postal_code
    }

    override fun bind(element: PostalCodeViewModel) {
        itemView.tvPostalCode.text = element.postalCode
    }
}