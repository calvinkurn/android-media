package com.tokopedia.deals.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.home.ui.adapter.viewholder.VoucherPlaceCardViewHolder
import com.tokopedia.deals.home.ui.dataview.VoucherPlaceCardDataView

class DealsVoucherPlaceCardAdapter(private val voucherPlaceCardListener: DealsVoucherPlaceCardListener) :
    RecyclerView.Adapter<VoucherPlaceCardViewHolder>() {

    var voucherPlaceCards: List<VoucherPlaceCardDataView> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(VoucherPlaceCardDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherPlaceCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_deals_voucher_place_card, parent, false)
        return VoucherPlaceCardViewHolder(itemView, voucherPlaceCardListener)
    }

    override fun getItemCount(): Int = voucherPlaceCards.size

    override fun onBindViewHolder(holder: VoucherPlaceCardViewHolder, position: Int) {
        holder.bindData(voucherPlaceCards[position])
    }

    private class VoucherPlaceCardDiffCallback(
        private val oldVoucherPlaceCards: List<VoucherPlaceCardDataView>,
        private val newVoucherPlaceCards: List<VoucherPlaceCardDataView>
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