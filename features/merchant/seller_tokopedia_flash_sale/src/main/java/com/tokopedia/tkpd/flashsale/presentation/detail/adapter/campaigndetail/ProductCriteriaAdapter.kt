package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCriteriaBinding
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel

class ProductCriteriaAdapter: RecyclerView.Adapter<ProductCriteriaAdapter.CriteriaViewHolder>() {

    var data: List<ProductCriteriaModel> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
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
                tfSubmissionLimit.value = "Maks. ${itemModel.maxProductSubmission}"
                tfMaxShown.value = "Maks. ${itemModel.maxShownCount} dalam ${itemModel.maxShownDays} hari"
                tfOtherCriteria.value = itemModel.otherCriteria.joinToString(", ")
                tfCategoryList.value = itemModel.categories.joinToString(", ")
                tfCategoryList.isVisible = itemModel.categories.isNotEmpty()
            }

        }

        private fun formatRange(valueRange: ProductCriteriaModel.ValueRange): String =
            "${valueRange.min} - ${valueRange.max}"
    }
}