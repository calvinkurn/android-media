package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.ItemDealsCategoryListBinding
import com.tokopedia.deals.ui.home.listener.DealsCategoryListener
import com.tokopedia.deals.ui.home.ui.adapter.DealsCategoryAdapter
import com.tokopedia.deals.ui.home.ui.dataview.CategoriesDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by jessica on 17/06/20
 */

class DealsHomeCategoriesViewHolder(itemView: View, private val categoryListener: DealsCategoryListener)
    : BaseViewHolder(itemView) {

    fun bind(categories: CategoriesDataView) {
        val binding = ItemDealsCategoryListBinding.bind(itemView)
        if (!categories.isLoaded) {
            binding.contentCategoriesShimmering.root.show()
            binding.rvDealsHomeCategories.hide()
        } else {
            with(binding) {
                contentCategoriesShimmering.root.hide()
                rvDealsHomeCategories.show()
                val categoriesAdapter = DealsCategoryAdapter(categoryListener)

                rvDealsHomeCategories.adapter = categoriesAdapter
                rvDealsHomeCategories.layoutManager = GridLayoutManager(root.context, DEALS_CATEGORY_SPAN_COUNT)

                categoriesAdapter.dealsCategories = categories.list

                if (categories.list.size > MAX_CATEGORY_COUNT) {
                    categoriesAdapter.enableSeeMoreCategory(MAX_CATEGORY_COUNT)
                }

                ViewCompat.setNestedScrollingEnabled(rvDealsHomeCategories, false)
            }
        }
    }

    companion object {
        const val DEALS_CATEGORY_SPAN_COUNT = 5
        const val MAX_CATEGORY_COUNT = 5
        val LAYOUT = R.layout.item_deals_category_list
    }
}
