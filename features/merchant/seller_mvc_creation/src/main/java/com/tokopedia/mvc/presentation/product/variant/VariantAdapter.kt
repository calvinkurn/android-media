package com.tokopedia.mvc.presentation.product.variant

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemVariantProductBinding
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.util.extension.grayscale
import com.tokopedia.mvc.util.extension.resetGrayscale
import com.tokopedia.unifyprinciples.Typography


class VariantAdapter : RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    private var onVariantClick: (Int) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<Product.Variant>() {
        override fun areItemsTheSame(oldItem: Product.Variant, newItem: Product.Variant): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItem: Product.Variant, newItem: Product.Variant): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SmvcItemVariantProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun setOnVariantClick(onVariantClick: (Int) -> Unit) {
        this.onVariantClick = onVariantClick
    }

    inner class ViewHolder(
        private val binding: SmvcItemVariantProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onVariantClick(bindingAdapterPosition) }
        }

        fun bind(item: Product.Variant) {
            with(binding) {
                checkbox.setOnCheckedChangeListener(null)

                tpgIneligibleReason.setIneligibleReason(item)

                imgProduct.loadRemoteImageUrl(item)

                tpgProductName.text = item.productName
                tpgProductName.isEnabled = item.isEligible


                tpgStock.text = binding.tpgStock.context.getString(R.string.smvc_placeholder_total_stock, item.stock.splitByThousand())
                tpgStock.isEnabled = item.isEligible

                tpgSoldCount.text = binding.tpgSoldCount.context.getString(R.string.smvc_placeholder_product_sold_count, 0)
                tpgSoldCount.isEnabled = item.isEligible

                tpgPrice.setPrice(item)
                tpgPrice.isEnabled = item.isEligible

                separator.isEnabled = item.isEligible

                checkbox.isChecked = item.isSelected
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onVariantClick(bindingAdapterPosition)
                }
                checkbox.isEnabled = item.isEligible
            }
        }

        private fun ImageView.loadRemoteImageUrl(item: Product.Variant) {
            loadImage("")

            if (item.isEligible) {
                resetGrayscale()
            } else {
                grayscale()
            }
        }

        private fun Typography.setIneligibleReason(item: Product.Variant) {
            if (item.reason.isNotEmpty()) {
                visible()
                text = item.reason
            } else {
                gone()
            }
        }

        private fun Typography.setPrice(item: Product.Variant) {
            text = context.getString(
                R.string.smvc_placeholder_product_price,
                item.price.splitByThousand()
            )
        }
    }

    fun submit(newVariants: List<Product.Variant>) {
        differ.submitList(newVariants)
    }

    fun snapshot(): List<Product.Variant> {
        return differ.currentList
    }
}
