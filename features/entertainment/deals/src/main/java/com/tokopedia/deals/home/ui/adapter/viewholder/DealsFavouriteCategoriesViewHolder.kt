package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.ui.adapter.DealsFavouriteCategoriesAdapter
import com.tokopedia.deals.home.ui.dataview.CuratedCategoryDataView
import com.tokopedia.kotlin.extensions.view.hide
import kotlinx.android.synthetic.main.item_deals_voucher_popular_place.view.*

/**
 * @author by jessica on 17/06/20
 */

class DealsFavouriteCategoriesViewHolder(itemView: View, private val listener: DealsFavouriteCategoriesListener)
    : BaseViewHolder(itemView) {

    fun bind(item: CuratedCategoryDataView) {
        with(itemView) {
            txt_voucher_popular_place_title.text = item.title
            if (item.subtitle.isNotEmpty()) {
                txt_voucher_popular_place_subtitle.text = item.subtitle
            } else txt_voucher_popular_place_subtitle.hide()

            val adapter = DealsFavouriteCategoriesAdapter(listener)
            lst_voucher_popular_place_card.adapter = adapter
            lst_voucher_popular_place_card.layoutManager = GridLayoutManager(context, DEALS_CATEGORY_SPAN_COUNT)
            adapter.voucherCuratedCategoryCards = item.curatedCategories

            ViewCompat.setNestedScrollingEnabled(lst_voucher_popular_place_card, false)
        }
        listener.onBindFavouriteCategory(item, adapterPosition)
    }

    companion object {
        const val DEALS_CATEGORY_SPAN_COUNT = 3
        val LAYOUT = R.layout.item_deals_voucher_popular_place
    }
}