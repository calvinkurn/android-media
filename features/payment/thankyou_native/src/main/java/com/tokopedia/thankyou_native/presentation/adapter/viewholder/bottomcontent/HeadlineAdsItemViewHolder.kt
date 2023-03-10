package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThanksItemTopAdsHeadlinesViewBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.HeadlineAdsWidgetModel
import com.tokopedia.utils.view.binding.viewBinding

class HeadlineAdsItemViewHolder(view: View?): AbstractViewHolder<HeadlineAdsWidgetModel>(view) {

    private var binding: ThanksItemTopAdsHeadlinesViewBinding? by viewBinding()

    companion object {
        val LAYOUT_ID = R.layout.thanks_item_top_ads_headlines_view
    }

    override fun bind(data: HeadlineAdsWidgetModel?) {
        if (data?.cpmModel == null) return

        binding?.topadsHeadlineView?.displayAds(data.cpmModel)
    }
}
