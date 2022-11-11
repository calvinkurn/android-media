package com.tokopedia.mvc.presentation.product.add.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemProductBinding
import com.tokopedia.unifyprinciples.Typography

class ProductDelegateAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onCheckboxClick: (Int, Boolean) -> Unit
) : DelegateAdapter<Product, ProductDelegateAdapter.ViewHolder>(
    Product::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = SmvcItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: Product, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding : SmvcItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bind(item: Product) {
            with(binding) {
                checkbox.setOnCheckedChangeListener(null)
                imgProduct.loadImage(item.picture)
                tpgProductName.text = item.name
                tpgSku.setSku(item)
                tpgStock.text = binding.tpgStock.context.getString(R.string.smvc_placeholder_total_stock, item.stock.splitByThousand())
                tpgSoldCount.text = binding.tpgSoldCount.context.getString(R.string.smvc_placeholder_product_sold_count, item.txStats.sold.splitByThousand())
                tpgPrice.setPrice(item)
                tpgIneligibleReason.setIneligibleReason(item)
                layoutVariant.setVariantCount(item)
                checkbox.isChecked = item.isSelected
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckboxClick(adapterPosition, isChecked)
                }
                checkbox.isEnabled = item.enableCheckbox
            }
        }

        private fun Typography.setIneligibleReason(item: Product) {
            if (item.ineligibleReason.isNotEmpty()) {
                visible()
                text = item.ineligibleReason
            } else {
                gone()
            }
        }

        private fun RelativeLayout.setVariantCount(item: Product) {
            isVisible = item.originalVariants.isNotEmpty()

            if (item.originalVariants.size != item.modifiedVariants.size) {
                binding.tpgVariantCount.text = binding.tpgVariantCount.context.getString(
                    R.string.smvc_placeholder_selected_variant_product_count,
                    item.modifiedVariants.size,
                    item.originalVariants.size
                )
            } else {
                binding.tpgVariantCount.text = binding.tpgVariantCount.context.getString(
                    R.string.smvc_placeholder_variant_product_count,
                    item.originalVariants.size
                )
            }
        }

        private fun Typography.setSku(item: Product) {
            if (item.sku.isEmpty()) {
                gone()
            } else {
                visible()
                text = context.getString(R.string.smvc_placeholder_product_sku, item.sku)
            }
        }

        private fun Typography.setPrice(item: Product) {
            text = if (item.price.min == item.price.max) {
                context.getString(
                    R.string.smvc_placeholder_product_price,
                    item.price.min.splitByThousand()
                )
            } else {
                context.getString(
                    R.string.smvc_placeholder_product_price_range,
                    item.price.min.splitByThousand(),
                    item.price.max.splitByThousand()
                )
            }
        }

    }

}
