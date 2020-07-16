package com.tokopedia.deals.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsFavouriteCategoryViewHolder
import com.tokopedia.deals.home.ui.dataview.FavouritePlacesDataView

/**
 * @author by jessica on 24/06/20
 */

class DealsFavouriteCategoriesAdapter(private val listener: DealsFavouriteCategoriesListener) :
        RecyclerView.Adapter<DealsFavouriteCategoryViewHolder>() {

    var voucherPlaceCards: List<FavouritePlacesDataView.Place> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(VoucherPlaceCardDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsFavouriteCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_voucher_place_card, parent, false)
        return DealsFavouriteCategoryViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = voucherPlaceCards.size

    override fun onBindViewHolder(holder: DealsFavouriteCategoryViewHolder, position: Int) {
        holder.bindData(voucherPlaceCards[position])
    }

    private class VoucherPlaceCardDiffCallback(
            private val oldVoucherPlaceCards: List<FavouritePlacesDataView.Place>,
            private val newVoucherPlaceCards: List<FavouritePlacesDataView.Place>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldVoucherPlaceCards.size

        override fun getNewListSize(): Int = newVoucherPlaceCards.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldVoucherPlaceCards[oldItemPosition].id == newVoucherPlaceCards[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldVoucherPlaceCards[oldItemPosition] == newVoucherPlaceCards[newItemPosition]
        }
    }
}
