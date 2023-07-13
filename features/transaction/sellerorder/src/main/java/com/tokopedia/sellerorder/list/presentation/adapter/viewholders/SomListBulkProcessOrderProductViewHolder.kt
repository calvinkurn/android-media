package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomListBulkProcessOrderProductBinding
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomListBulkProcessOrderProductViewHolder(itemView: View) : AbstractViewHolder<SomListBulkProcessOrderProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_bulk_process_order_product
    }

    private val binding by viewBinding<ItemSomListBulkProcessOrderProductBinding>()

    @SuppressLint("SetTextI18n")
    @Suppress("NAME_SHADOWING")
    override fun bind(element: SomListBulkProcessOrderProductUiModel?) {
        element?.let { element ->
            binding?.run {
                ivProduct.loadProductImage(
                    url = element.picture,
                    archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_SMALL,
                    cornerRadius = 5f
                )
                val productName = element.productName.split(" - ").firstOrNull().orEmpty().trim()
                val productVariant = element.productName.split(" - ").takeIf { it.size > 1 }?.lastOrNull().orEmpty().replace(Regex("\\s*,\\s*"), " | ").trim()
                tvProductName.apply {
                    if (productVariant.isBlank()) {
                        maxLines = 2
                        isSingleLine = false
                    } else {
                        maxLines = 1
                        isSingleLine = true
                    }
                    text = productName
                }
                tvProductVariant.apply {
                    text = productVariant
                    showWithCondition(productVariant.isNotBlank())
                }
                tvProductCount.text = getString(R.string.som_list_bulk_accept_order_product_amount, element.amount.toString())
            }
        }
    }
}