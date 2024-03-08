package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.ItemDealsCategoryBinding
import com.tokopedia.deals.ui.home.listener.DealsCategoryListener
import com.tokopedia.deals.ui.home.ui.dataview.DealsCategoryDataView
import com.tokopedia.kotlin.extensions.view.loadImage

class DealsCategoryViewHolder(val binding: ItemDealsCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(
            dealsCategory: DealsCategoryDataView,
            dealsCategoryListener: DealsCategoryListener
    ) {
        binding.run {
            btnDealsCategory.loadImage(dealsCategory.imageUrl)
            btnDealsCategory.setOnClickListener {
                dealsCategoryListener.onDealsCategoryClicked(dealsCategory, adapterPosition)
            }
            txtDealsCategoryTitle.text = dealsCategory.title
        }
    }

    fun bindOnSeeAllCategories(categories: List<DealsCategoryDataView>,
                               dealsCategoryListener: DealsCategoryListener) {
        with(binding) {
            btnDealsCategory.setImageDrawable(ContextCompat.getDrawable(binding.root.context,
                    R.drawable.ic_deals_all_other_categories))
            btnDealsCategory.setOnClickListener {
                dealsCategoryListener.onDealsCategorySeeAllClicked(categories)
            }
            txtDealsCategoryTitle.text = binding.root.resources.getString(R.string.deals_homepage_category_see_all)
        }
    }
}
