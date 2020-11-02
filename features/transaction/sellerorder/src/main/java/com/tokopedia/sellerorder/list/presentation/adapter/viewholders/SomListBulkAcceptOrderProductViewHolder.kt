package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderProductUiModel
import kotlinx.android.synthetic.main.item_som_list_bulk_accept_order.view.*

class SomListBulkAcceptOrderProductViewHolder(itemView: View?) : AbstractViewHolder<SomListBulkAcceptOrderProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_bulk_accept_order
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: SomListBulkAcceptOrderProductUiModel?) {
        element?.let { element ->
            with(itemView) {
                ivProduct.loadImageRounded(element.picture)
                tvProductName.text = element.productName
                tvProductCount.text = getString(R.string.som_list_bulk_accept_order_product_amount, element.amount.toString())
            }
        }
    }
}