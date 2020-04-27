package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductLastSeenDataModel
import kotlinx.android.synthetic.main.item_dynamic_pdp_last_seen.view.*

data class ProductLastSeenViewHolder(private val view: View) : AbstractViewHolder<ProductLastSeenDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_last_seen
    }

    override fun bind(element: ProductLastSeenDataModel?) {
        view.txt_last_update.show()
        view.txt_last_update.text = getString(R.string.template_last_update_price, element?.lastSeen)
    }

}