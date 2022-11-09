package com.tokopedia.mvc.presentation.product.add

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.seller_mvc_creation.R
import com.tokopedia.seller_mvc_creation.databinding.SmvcItemProductBinding
import com.tokopedia.unifyprinciples.Typography

class ProductDelegateAdapter(
    private val onItemClick: (Int) -> Unit,
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
                imgProduct.loadImage(item.picture)
                tpgProductName.text = item.name
                tpgSku.setSku(item)
                tpgStock.text = binding.tpgStock.context.getString(R.string.smvc_placeholder_total_stock, item.stock)
                tpgSoldCount.text = binding.tpgSoldCount.context.getString(R.string.smvc_placeholder_product_sold_count, item.txStats.sold)
                tpgPrice.setPrice(item)
                tpgIneligibleReason.setIneligibleReason(item)
                layoutVariant.setVariantCount(item)
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
            isVisible = item.variants.isNotEmpty()
            binding.tpgVariantCount.text = binding.tpgVariantCount.context.getString(
                R.string.smvc_placeholder_variant_product_count,
                item.variants.size
            )
        }

        private fun Typography.setSku(item: Product) {
            if (item.sku.isEmpty()) {
                invisible()
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
