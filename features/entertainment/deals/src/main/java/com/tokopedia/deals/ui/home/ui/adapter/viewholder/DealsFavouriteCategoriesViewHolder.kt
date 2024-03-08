package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.ItemDealsVoucherPopularPlaceBinding
import com.tokopedia.deals.ui.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.ui.home.ui.adapter.DealsFavouriteCategoriesAdapter
import com.tokopedia.deals.ui.home.ui.dataview.CuratedCategoryDataView
import com.tokopedia.kotlin.extensions.view.hide

/**
 * @author by jessica on 17/06/20
 */

class DealsFavouriteCategoriesViewHolder(itemView: View, private val listener: DealsFavouriteCategoriesListener)
    : BaseViewHolder(itemView) {

    fun bind(item: CuratedCategoryDataView) {
        val binding = ItemDealsVoucherPopularPlaceBinding.bind(itemView)
        with(binding){
            txtVoucherPopularPlaceTitle.text = item.title
            if (item.subtitle.isNotEmpty()) {
                txtVoucherPopularPlaceSubtitle.text = item.subtitle
            } else txtVoucherPopularPlaceSubtitle.hide()

            val adapter = DealsFavouriteCategoriesAdapter(listener)
            lstVoucherPopularPlaceCard.adapter = adapter
            lstVoucherPopularPlaceCard.layoutManager = GridLayoutManager(binding.root.context, DEALS_CATEGORY_SPAN_COUNT)
            adapter.voucherCuratedCategoryCards = item.curatedCategories

            ViewCompat.setNestedScrollingEnabled(lstVoucherPopularPlaceCard, false)

        }
        listener.onBindFavouriteCategory(item, adapterPosition)
    }

    companion object {
        const val DEALS_CATEGORY_SPAN_COUNT = 3
        val LAYOUT = R.layout.item_deals_voucher_popular_place
    }
}
