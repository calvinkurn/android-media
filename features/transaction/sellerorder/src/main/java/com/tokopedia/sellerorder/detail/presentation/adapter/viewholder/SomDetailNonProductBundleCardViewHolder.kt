package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductCardItemBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

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

    private val binding by viewBinding<DetailProductCardItemBinding>()

    override fun bind(element: NonProductBundleUiModel) {
        val product = element.product
        binding?.run {
            root.setOnClickListener {
                actionListener?.onClickProduct(product.orderDetailId.toIntOrZero())
            }
            ivProduct.loadImage(product.thumbnail)
            tvProductName.text = product.name
            tvProductDesc.text = StringBuilder("${product.quantity} x ${product.priceText}")
            if (product.note.isNotEmpty()) {
                dividerProduct.visibility = View.VISIBLE
                tvProductNotes.visibility = View.VISIBLE
                tvProductNotes.text = product.note.replace("\\n", System.getProperty("line.separator")
                        ?: "")
            } else {
                dividerProduct.visibility = View.GONE
                tvProductNotes.visibility = View.GONE
            }
        }
    }
}