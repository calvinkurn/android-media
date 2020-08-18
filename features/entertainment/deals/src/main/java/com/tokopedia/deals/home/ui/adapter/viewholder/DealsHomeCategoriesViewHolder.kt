package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsCategoryListener
import com.tokopedia.deals.home.ui.adapter.DealsCategoryAdapter
import com.tokopedia.deals.home.ui.dataview.CategoriesDataView
import com.tokopedia.deals.home.ui.fragment.DealsCategoryBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_category_list.view.*

/**
 * @author by jessica on 17/06/20
 */

class DealsHomeCategoriesViewHolder(itemView: View, private val categoryListener: DealsCategoryListener)
    : BaseViewHolder(itemView) {

    fun bind(categories: CategoriesDataView) {
        if (!categories.isLoaded) {
            itemView.contentCategoriesShimmering.show()
            itemView.rvDealsHomeCategories.hide()
        } else {
            with(itemView) {
                contentCategoriesShimmering.hide()
                rvDealsHomeCategories.show()
                val categoriesAdapter = DealsCategoryAdapter(categoryListener)

                rvDealsHomeCategories.adapter = categoriesAdapter
                rvDealsHomeCategories.layoutManager = GridLayoutManager(context, DEALS_CATEGORY_SPAN_COUNT)

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