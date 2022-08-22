package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCriteriaBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel

class ProductCriteriaViewHolder (private val binding: StfsItemProductCriteriaBinding) :
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