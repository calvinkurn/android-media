package com.tokopedia.topads.view.adapter.bidinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoUiModel
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoViewHolder

class BidInfoAdapter(private val typeFactory: BindInfoAdapterTypeFactory) : RecyclerView.Adapter<BidInfoViewHolder<BidInfoUiModel>>() {

    var items: MutableList<BidInfoUiModel> = mutableListOf()
    var minBid: String = "0"

    override fun onBindViewHolder(holder: BidInfoViewHolder<BidInfoUiModel>, position: Int) {
        holder.bind(items[position],minBid)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidInfoViewHolder<BidInfoUiModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as BidInfoViewHolder<BidInfoUiModel>
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setMinimumBid(bid: String) {
        minBid = bid
        notifyDataSetChanged()
    }

}
