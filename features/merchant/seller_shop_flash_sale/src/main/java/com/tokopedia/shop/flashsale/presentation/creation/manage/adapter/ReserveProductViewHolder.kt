package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemReserveProductBinding
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.utils.view.binding.viewBinding

class ReserveProductViewHolder(
    itemView: View,
    private val itemOnClick: (position: Int, value: Boolean) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ssfs_item_reserve_product, parent, false)
    }

    private val binding: SsfsItemReserveProductBinding? by viewBinding()

    fun bind(item: ReserveProductModel) {
        binding?.apply {
            tvProductName.text = item.productName
            imgProduct.loadImage(item.imageUrl)
            tvSku.text = "SKU: " + if (item.sku.isBlank()) "-" else item.sku
            tvStock.text = "Total stok: ${item.stock}"
            tvProductPrice.text = item.price.getCurrencyFormatted()
            labelVariantCount.text = "${item.variantCount} Varian Produk"
            labelVariantCount.isVisible = item.variantCount.isMoreThanZero()
            checkboxItem.isChecked = item.isSelected
            root.setOnClickListener {
                checkboxItem.isChecked = !checkboxItem.isChecked
                itemOnClick(adapterPosition, checkboxItem.isChecked)
            }
            checkboxItem.setOnClickListener {
                itemOnClick(adapterPosition, checkboxItem.isChecked)
            }
        }
    }
}
