package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsCategoryListener
import com.tokopedia.deals.home.ui.dataview.DealsCategoryDataView
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_deals_category.view.*

class DealsCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(
            dealsCategory: DealsCategoryDataView,
            dealsCategoryListener: DealsCategoryListener
    ) {
        itemView.run {
            btn_deals_category.loadImage(dealsCategory.imageUrl)
            btn_deals_category.setOnClickListener {
                dealsCategoryListener.onDealsCategoryClicked(dealsCategory, adapterPosition)
            }
            txt_deals_category_title.text = dealsCategory.title
        }
    }

    fun bindOnSeeAllCategories(categories: List<DealsCategoryDataView>,
                               dealsCategoryListener: DealsCategoryListener) {
        with(itemView) {
            btn_deals_category.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_deals_all_other_categories))
            btn_deals_category.setOnClickListener {
                dealsCategoryListener.onDealsCategorySeeAllClicked(categories)
            }
            txt_deals_category_title.text = resources.getString(R.string.deals_homepage_category_see_all)
        }
    }
}