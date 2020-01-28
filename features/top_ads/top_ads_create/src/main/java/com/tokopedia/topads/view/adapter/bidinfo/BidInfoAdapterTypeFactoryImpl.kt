package com.tokopedia.topads.view.adapter.bidinfo

import android.view.View
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoEmptyViewHolder
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoItemViewHolder
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoViewHolder
import kotlin.reflect.KFunction0

class BidInfoAdapterTypeFactoryImpl(var selectedKeywords: MutableList<String>, var selectedSuggestBid: MutableList<Int>, var actionClose: (pos: Int) -> Unit) : BindInfoAdapterTypeFactory {

    override fun type(model: BidInfoEmptyViewModel): Int {
        return BidInfoEmptyViewHolder.LAYOUT
    }

    override fun type(model: BidInfoItemViewModel): Int {
        return BidInfoItemViewHolder.LAYOUT
    }


    override fun holder(type: Int, view: View): BidInfoViewHolder<*> {
        return when (type) {
            BidInfoEmptyViewHolder.LAYOUT -> BidInfoEmptyViewHolder(view)
            BidInfoItemViewHolder.LAYOUT -> BidInfoItemViewHolder(view, selectedKeywords, selectedSuggestBid, actionClose)
            else -> throw RuntimeException("Illegal view type")

        }

    }

}

