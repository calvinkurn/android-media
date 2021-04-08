package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BidInfoViewHolder<in T>(view: View): RecyclerView.ViewHolder(view){
    open fun bind(item: T, minBid: String) {}
}