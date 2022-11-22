package com.tokopedia.mvc.presentation.product.variant.review

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemReviewVariantProductBinding
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.extension.grayscale
import com.tokopedia.mvc.util.extension.resetGrayscale
import com.tokopedia.unifyprinciples.Typography


class ReviewVariantAdapter : RecyclerView.Adapter<ReviewVariantAdapter.ViewHolder>() {

    private var onVariantClick: (Int, Boolean) -> Unit = { _, _ -> }
    private var onDeleteVariantClick: (Int) -> Unit = {}

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
            SmvcItemReviewVariantProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setOnDeleteVariantClick(onDeleteVariantClick: (Int) -> Unit) {
        this.onDeleteVariantClick = onDeleteVariantClick
    }


    inner class ViewHolder(
        private val binding: SmvcItemReviewVariantProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconDelete.setOnClickListener { onDeleteVariantClick(bindingAdapterPosition) }
        }

        fun bind(item: Variant) {
            with(binding) {
                checkBox.setOnCheckedChangeListener(null)

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
                checkBox.isVisible = item.isCheckable

                tpgSoldCount.text = binding.tpgSoldCount.context.getString(R.string.smvc_placeholder_product_sold_count, item.soldCount.splitByThousand())
                tpgSoldCount.isEnabled = item.isEligible

                iconDelete.isVisible = item.isDeletable
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
