package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import kotlinx.android.synthetic.main.detail_product_card_item.view.*

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailNonProductBundleCardViewHolder(
        private val actionListener: SomDetailAdapter.ActionListener?,
        itemView: View?
) : AbstractViewHolder<NonProductBundleUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.detail_product_card_item
    }

    override fun bind(element: NonProductBundleUiModel) {
        val product = element.product
        with(itemView) {
            setOnClickListener {
                actionListener?.onClickProduct(product.orderDetailId.toIntOrZero())
            }
            iv_product.loadImage(product.thumbnail)
            tv_product_name.text = product.name
            tv_product_desc.text = StringBuilder("${product.quantity} x ${product.priceText}")
            if (product.note.isNotEmpty()) {
                divider_product.visibility = View.VISIBLE
                tv_product_notes.visibility = View.VISIBLE
                tv_product_notes.text = product.note.replace("\\n", System.getProperty("line.separator")
                        ?: "")
            } else {
                divider_product.visibility = View.GONE
                tv_product_notes.visibility = View.GONE
            }
        }
    }
}