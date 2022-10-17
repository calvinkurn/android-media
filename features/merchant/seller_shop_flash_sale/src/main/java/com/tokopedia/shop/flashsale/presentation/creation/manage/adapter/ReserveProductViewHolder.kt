package com.tokopedia.shop.flashsale.presentation.creation.manage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemReserveProductBinding
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel
import com.tokopedia.utils.view.binding.viewBinding

class ReserveProductViewHolder(
    itemView: View,
    private val itemOnClick: (position: Int, value: Boolean) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val ENABLED_ALPHA = 1F
        private const val DISABLED_ALPHA = 0.5F
        private const val DEFAULT_EMPTY_PLACEHOLDER_TEXT = "-"

        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ssfs_item_reserve_product, parent, false)
    }

    private val binding: SsfsItemReserveProductBinding? by viewBinding()

    init {
        binding?.apply {
            root.setOnClickListener {
                checkboxItem.isChecked = !checkboxItem.isChecked
                itemOnClick(adapterPosition, checkboxItem.isChecked)
            }
            checkboxItem.setOnClickListener {
                itemOnClick(adapterPosition, checkboxItem.isChecked)
            }
        }
    }

    fun bind(
        item: ReserveProductModel,
        selectedProducts: MutableList<SelectedProductModel>,
        inputEnabled: Boolean
    ) {
        val context = itemView.context
        val skuValue = if (item.sku.isNotEmpty()) item.sku else DEFAULT_EMPTY_PLACEHOLDER_TEXT
        val skuText = context.getString(R.string.chooseproduct_sku_text, skuValue)
        val stockText = context.getString(R.string.chooseproduct_stock_total_text, item.stock)
        val variantText = context.getString(R.string.chooseproduct_variant_text, item.variant.size.dec())
        val isSelected = selectedProducts.any { it.productId == item.productId }

        binding?.apply {
            tvProductName.text = item.productName
            imgProduct.loadImage(item.imageUrl)
            tvSku.text = skuText
            tvStock.text = stockText
            tvProductPrice.text = item.price.getCurrencyFormatted()
            labelVariantCount.text = variantText
            labelVariantCount.isVisible = item.variant.size.isMoreThanZero()
            checkboxItem.isChecked = isSelected
            tvDisabledReason.setTextAndCheckShow(item.disabledReason)
        }

        if (item.disabled) {
            // set from item disability
            setListEnable(false)
        } else if (!isSelected) {
            // set from adapter input enabled
            setListEnable(inputEnabled)
        } else {
            setListEnable(true)
        }
    }

    private fun setListEnable(enabled: Boolean) {
        binding?.apply {
            tvProductName.isEnabled = enabled
            imgProduct.alpha = if (enabled) ENABLED_ALPHA else DISABLED_ALPHA
            tvSku.isEnabled = enabled
            tvStock.isEnabled = enabled
            tvProductPrice.isEnabled = enabled
            labelVariantCount.isEnabled = enabled
            labelVariantCount.isEnabled = enabled
            checkboxItem.isEnabled = enabled
            root.isEnabled = enabled
        }
    }
}
