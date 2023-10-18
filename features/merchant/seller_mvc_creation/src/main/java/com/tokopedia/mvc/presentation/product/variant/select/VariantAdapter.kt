package com.tokopedia.mvc.presentation.product.variant.select

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
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.extension.grayscale
import com.tokopedia.mvc.util.extension.resetGrayscale
import com.tokopedia.unifyprinciples.Typography


class VariantAdapter : RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    private var onVariantClick: (Int, Boolean) -> Unit = { _, _ -> }

    private val differCallback = object : DiffUtil.ItemCallback<Variant>() {
        override fun areItemsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem.variantId == newItem.variantId
        }

        override fun areContentsTheSame(oldItem: Variant, newItem: Variant): Boolean {
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

    fun setOnVariantClick(onVariantClick: (Int, Boolean) -> Unit) {
        this.onVariantClick = onVariantClick
    }

    inner class ViewHolder(
        private val binding: SmvcItemVariantProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Variant) {
            with(binding) {
                checkBox.setOnCheckedChangeListener(null)

                tpgIneligibleReason.setIneligibleReason(item)

                imgVariant.loadRemoteImageUrl(item)
                imgVariant.cornerRadius = NumberConstant.IMAGE_VIEW_CORNER_RADIUS

                tpgVariantName.text = item.variantName
                tpgVariantName.isEnabled = item.isEligible

                tpgStock.text = binding.tpgStock.context.getString(R.string.smvc_placeholder_total_stock, item.stockCount.splitByThousand())
                tpgStock.isEnabled = item.isEligible

                tpgPrice.setPrice(item)
                tpgPrice.isEnabled = item.isEligible

                checkBox.isChecked = item.isSelected
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onVariantClick(bindingAdapterPosition, isChecked)
                }
                checkBox.isEnabled = item.isEligible

                tpgSoldCount.text = binding.tpgSoldCount.context.getString(R.string.smvc_placeholder_product_sold_count, item.soldCount.splitByThousand())
                tpgSoldCount.isEnabled = item.isEligible
            }
        }

        private fun ImageView.loadRemoteImageUrl(item: Variant) {
            loadImage(item.imageUrl)

            if (item.isEligible) {
                resetGrayscale()
            } else {
                grayscale()
            }
        }

        private fun Typography.setIneligibleReason(item: Variant) {
            if (item.reason.isNotEmpty()) {
                visible()
                text = item.reason
            } else {
                gone()
            }
        }

        private fun Typography.setPrice(item: Variant) {
            text = context.getString(
                R.string.smvc_placeholder_product_price,
                item.price.splitByThousand()
            )
        }
    }

    fun submit(newVariants: List<Variant>) {
        differ.submitList(newVariants)
    }

    fun snapshot(): List<Variant> {
        return differ.currentList
    }
}
