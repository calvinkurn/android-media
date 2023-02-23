package com.tokopedia.mvc.presentation.product.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemProductListBinding
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.extension.grayscale
import com.tokopedia.mvc.util.extension.resetGrayscale
import com.tokopedia.unifyprinciples.Typography

class ProductListAdapter(
    private val onDeleteProductClick: (Int) -> Unit,
    private val onCheckboxClick: (Int, Boolean) -> Unit,
    private val onVariantClick: (Int) -> Unit
)  : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder(private val binding : SmvcItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconDelete.setOnClickListener { onDeleteProductClick(bindingAdapterPosition) }
            binding.layoutVariant.setOnClickListener { onVariantClick(bindingAdapterPosition) }
        }

        fun bind(item: Product) {
            with(binding) {
                checkbox.setOnCheckedChangeListener(null)

                imgProduct.loadRemoteImageUrl(item)
                imgProduct.cornerRadius = NumberConstant.IMAGE_VIEW_CORNER_RADIUS

                tpgProductName.text = item.name
                tpgProductName.isEnabled = item.isEligible

                tpgSku.setSku(item)
                tpgSku.isEnabled = item.isEligible

                tpgStock.text = binding.tpgStock.context.getString(R.string.smvc_placeholder_total_stock, item.stock.splitByThousand())
                tpgStock.isEnabled = item.isEligible

                tpgSoldCount.text = binding.tpgSoldCount.context.getString(R.string.smvc_placeholder_product_sold_count, item.txStats.sold.splitByThousand())
                tpgSoldCount.isEnabled = item.isEligible

                tpgPrice.setPrice(item)
                tpgPrice.isEnabled = item.isEligible

                layoutVariant.setVariantCount(item)
                layoutVariant.isEnabled = item.isEligible

                separator.isEnabled = item.isEligible

                checkbox.isChecked = item.isSelected
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckboxClick(bindingAdapterPosition, isChecked)
                }
                checkbox.isEnabled = item.enableCheckbox && item.isEligible

                checkbox.isVisible = item.enableCheckbox

                iconDelete.isVisible = item.isDeletable
            }
        }

        private fun ImageView.loadRemoteImageUrl(item: Product) {
            loadImage(item.picture)

            if (item.isEligible) {
                resetGrayscale()
            } else {
                grayscale()
            }
        }

        private fun RelativeLayout.setVariantCount(item: Product) {
            isVisible = item.selectedVariantsIds.isNotEmpty()
            val variantCount = item.selectedVariantsIds.count()

            binding.tpgVariantCount.text = binding.tpgVariantCount.context.getString(
                R.string.smvc_placeholder_variant_product_count,
                variantCount
            )
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

    fun submit(newVariants: List<Product>) {
        differ.submitList(newVariants)
    }

    fun snapshot(): List<Product> {
        return differ.currentList
    }
}
