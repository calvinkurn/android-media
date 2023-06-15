package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankBottomContentTopadsBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.utils.view.binding.viewBinding

class TopAdsItemViewHolder(view: View?): AbstractViewHolder<TopAdsRequestParams>(view) {

    private var binding: ThankBottomContentTopadsBinding? by viewBinding()

    companion object {
        val LAYOUT_ID = R.layout.thank_bottom_content_topads
    }

    override fun bind(data: TopAdsRequestParams?) {
        if (data == null) return

        val topAdsView = binding?.topAdsView
        if (!data.topAdsUIModelList.isNullOrEmpty()) {
            topAdsView?.visible()
            topAdsView?.addData(data)
        } else {
            topAdsView?.gone()
        }
    }
}
