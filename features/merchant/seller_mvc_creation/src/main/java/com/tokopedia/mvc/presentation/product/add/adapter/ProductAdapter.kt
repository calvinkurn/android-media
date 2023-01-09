package com.tokopedia.mvc.presentation.product.add.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemProductBinding
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.extension.grayscale
import com.tokopedia.mvc.util.extension.resetGrayscale
import com.tokopedia.unifyprinciples.Typography

class ProductAdapter(
    private val onCheckboxClick: (Int, Boolean) -> Unit,
    private val onCtaChangeVariantClick: (Int) -> Unit,
    private val onVariantClick: (Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

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
        val binding = SmvcItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder(private val binding : SmvcItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tpgUpdateVariant.setOnClickListener { onCtaChangeVariantClick(bindingAdapterPosition) }
            binding.layoutVariant.setOnClickListener { onVariantClick(bindingAdapterPosition) }
        }

        fun bind(item: Product) {
            with(binding) {
                checkbox.setOnCheckedChangeListener(null)

                tpgIneligibleReason.setIneligibleReason(item)

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

                when {
                    item.isSelected && item.selectedVariantsIds.count() != item.originalVariants.count() -> {
                        checkbox.isChecked = true
                        checkbox.setIndeterminate(true)
                    }
                    item.isSelected && item.selectedVariantsIds.count() == item.originalVariants.count() -> {
                        checkbox.isChecked = true
                        checkbox.setIndeterminate(false)
                    }
                    !item.isSelected -> {
                        checkbox.isChecked = false
                        checkbox.setIndeterminate(false)
                    }
                }

                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckboxClick(bindingAdapterPosition, isChecked)
                }
                checkbox.isEnabled = item.enableCheckbox && item.isEligible
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
            val originalVariantCount = item.originalVariants.count()
            if (item.isSelected) {
                binding.iconDropdown.gone()
                binding.tpgUpdateVariant.visible()
                binding.tpgVariantCount.text = MethodChecker.fromHtml(
                    binding.tpgVariantCount.context.getString(
                        R.string.smvc_placeholder_selected_variant_product_count,
                        item.selectedVariantsIds.size,
                        item.originalVariants.size
                    )
                )
            } else {
                binding.iconDropdown.visible()
                binding.tpgUpdateVariant.gone()
                binding.tpgVariantCount.text = binding.tpgVariantCount.context.getString(
                    R.string.smvc_placeholder_variant_product_count,
                    originalVariantCount
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

    fun submit(newVariants: List<Product>) {
        differ.submitList(newVariants)
    }

    fun snapshot(): List<Product> {
        return differ.currentList
    }
}
