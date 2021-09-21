package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class BidInfoEmptyViewHolder(val view: View) : BidInfoViewHolder<BidInfoEmptyViewModel>(view) {

    var imageView = view.findViewById<ImageUnify>(R.id.image_empty)
    var titleText = view.findViewById<Typography>(R.id.text_title)
    var descText = view.findViewById<Typography>(R.id.text_desc)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_insight_empty_layout
    }



    override fun bind(item: BidInfoEmptyViewModel, minBid: String) {
        imageView.setImageDrawable(view.context.getResDrawable(R.drawable.ic_empty_keyword))
        titleText.text = view.context.getString(R.string.topads_empty_insight_title)
        descText.text = view.context.getString(R.string.topads_empty_insight_desc)
    }


}