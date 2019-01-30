package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoCategoryViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_detail_category.view.*
import kotlinx.android.synthetic.main.partial_criteria_detail.view.*

class CampaignInfoCategoryViewHolder(val view: View): AbstractViewHolder<CampaignInfoCategoryViewModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_detail_category
    }
    override fun bind(element: CampaignInfoCategoryViewModel) {
        val criteria = element.criteria
        itemView.run {
            expandable_arrow.setTitleText("${adapterPosition-1}. ${criteria.categories[0].depName}")
            discount_range.text = context.getString(R.string.range_format, criteria.minPrice, criteria.maxPrice)
            discount.text = context.getString(R.string.range_format,
                    "${criteria.minDiscount}%", "${criteria.maxDiscount}%")

            rating.text = context.getString(R.string.range_format, "0", criteria.maxRating.toString())
            limit_request.text = context.getString(R.string.submission_count_format, criteria.maxSubmission)
            min_stock.text = criteria.minStock.toString()
            min_cashback.text = if (criteria.minCashback == 0) "-" else criteria.minCashback.toString()

            val noteItems = mutableListOf<String>()
            if (criteria.isPreorderExcluded) noteItems.add("PreOrder")
            if (criteria.isWholesaleExcluded) noteItems.add("Grosir")

            if (noteItems.size > 0)
                notes.text = noteItems.joinToString(separator = " & ", prefix = "Tidak ")
            else
                notes.text = ""
        }
    }
}