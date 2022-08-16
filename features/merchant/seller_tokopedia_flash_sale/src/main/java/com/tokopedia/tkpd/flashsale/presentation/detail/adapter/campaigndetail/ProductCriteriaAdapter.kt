package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCriteriaBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel

class ProductCriteriaAdapter: RecyclerView.Adapter<ProductCriteriaAdapter.CriteriaViewHolder>() {

    var data: List<ProductCriteriaModel> = mutableListOf()
        set(value) {
            val diffUtil = CriteriaDiffUtil(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemProductCriteriaBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    inner class CriteriaDiffUtil(
        private val oldItems: List<ProductCriteriaModel>,
        private val newItems: List<ProductCriteriaModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old.categorySelectionsText == new.categorySelectionsText
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }

    inner class CriteriaViewHolder(private val binding: StfsItemProductCriteriaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconExpand.setOnClickListener {
                binding.layoutContent.root.apply {
                    isVisible = !isVisible
                    setExpandIcon(isVisible)
                }
            }
        }

        private fun setExpandIcon(isContentVisible: Boolean) {
            binding.iconExpand.setImage(if (isContentVisible) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            })
        }

        fun bind(itemModel: ProductCriteriaModel) {
            val context = binding.root.context
            binding.tfCategoryTitle.text = itemModel.categorySelectionsText
            binding.tfProductCount.text = itemModel.productSelectionsText

            with(binding.layoutContent) {
                tfOriginalPrice.value = formatRange(itemModel.originalPriceRange)
                tfDiscount.value = "${itemModel.minDiscount}%"
                tfDiscountedPrice.value = formatRange(itemModel.discountedPriceRange)
                tfCampaignStock.value = formatRange(itemModel.stockCampaignRange)
                tfMinRating.value = itemModel.minRating.toString()
                tfMinScore.value = itemModel.minScore.toString()
                tfProductSold.value = formatRange(itemModel.productSold)
                tfMinProductSold.value = itemModel.minSold.toString()
                tfSubmissionLimit.value = context.getString(R.string.campaigndetail_submission_limit_format, itemModel.maxProductSubmission)
                tfMaxShown.value = context.getString(R.string.campaigndetail_max_shown_format, itemModel.maxShownCount, itemModel.maxShownDays)
                tfOtherCriteria.value = itemModel.otherCriteria.joinToString(", ")
                tfCategoryList.value = itemModel.categories.joinToString(", ")
                tfCategoryList.isVisible = itemModel.showFullCategories
            }

        }

        private fun formatRange(valueRange: ProductCriteriaModel.ValueRange): String =
            "${valueRange.min} - ${valueRange.max}"
    }
}