package com.tokopedia.deals.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsFavouriteCategoryViewHolder
import com.tokopedia.deals.home.ui.dataview.CuratedCategoryDataView

/**
 * @author by jessica on 24/06/20
 */

class DealsFavouriteCategoriesAdapter(private val listener: DealsFavouriteCategoriesListener) :
        RecyclerView.Adapter<DealsFavouriteCategoryViewHolder>() {

    var voucherCuratedCategoryCards: List<CuratedCategoryDataView.CuratedCategory> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(VoucherPlaceCardDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsFavouriteCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_deals_voucher_place_card, parent, false)
        return DealsFavouriteCategoryViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = voucherCuratedCategoryCards.size

    override fun onBindViewHolder(holder: DealsFavouriteCategoryViewHolder, position: Int) {
        holder.bindData(voucherCuratedCategoryCards[position],position)
    }

    private class VoucherPlaceCardDiffCallback(
            private val oldVoucherCuratedCategoryCards: List<CuratedCategoryDataView.CuratedCategory>,
            private val newVoucherCuratedCategoryCards: List<CuratedCategoryDataView.CuratedCategory>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldVoucherCuratedCategoryCards.size

        override fun getNewListSize(): Int = newVoucherCuratedCategoryCards.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldVoucherCuratedCategoryCards[oldItemPosition].id == newVoucherCuratedCategoryCards[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldVoucherCuratedCategoryCards[oldItemPosition] == newVoucherCuratedCategoryCards[newItemPosition]
        }
    }
}
