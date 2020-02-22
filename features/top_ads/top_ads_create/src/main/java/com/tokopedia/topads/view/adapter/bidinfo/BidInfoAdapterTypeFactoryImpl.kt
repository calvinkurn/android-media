package com.tokopedia.topads.view.adapter.bidinfo

import android.view.View
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoEmptyViewHolder
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoItemViewHolder
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoViewHolder

class BidInfoAdapterTypeFactoryImpl(var selectedKeywords: MutableList<String>, var selectedSuggestBid: MutableList<Int>, private var actionClose: (pos: Int) -> Unit, var actionClick: () -> Int) : BindInfoAdapterTypeFactory {

    override fun type(model: BidInfoEmptyViewModel): Int {
        return BidInfoEmptyViewHolder.LAYOUT
    }

    override fun type(model: BidInfoItemViewModel): Int {
        return BidInfoItemViewHolder.LAYOUT
    }

    override fun holder(type: Int, view: View): BidInfoViewHolder<*> {
        return when (type) {
            BidInfoEmptyViewHolder.LAYOUT -> BidInfoEmptyViewHolder(view)
            BidInfoItemViewHolder.LAYOUT -> BidInfoItemViewHolder(view, selectedKeywords, selectedSuggestBid, actionClose, actionClick)
            else -> throw RuntimeException("Illegal view type")

        }

    }

}

