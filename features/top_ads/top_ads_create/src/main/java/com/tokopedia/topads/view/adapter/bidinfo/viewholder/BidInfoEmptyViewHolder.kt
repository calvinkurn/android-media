package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel

class BidInfoEmptyViewHolder(view: View):BidInfoViewHolder<BidInfoEmptyViewModel>(view){

    companion object{
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_no_keyword_selected_bid
    }

    override fun bind(item: BidInfoEmptyViewModel) {
    }

}