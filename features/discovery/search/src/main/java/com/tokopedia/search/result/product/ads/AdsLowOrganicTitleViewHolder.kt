package com.tokopedia.search.result.product.ads

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchAdsLowOrganicTitleBinding
import com.tokopedia.utils.view.binding.viewBinding

class AdsLowOrganicTitleViewHolder(
    itemView: View
): AbstractViewHolder<AdsLowOrganicTitleDataView>(itemView) {

    private var binding: SearchAdsLowOrganicTitleBinding? by viewBinding()

    override fun bind(element: AdsLowOrganicTitleDataView) {
        binding?.searchAdsLowOrganicTitle?.text =
            getString(R.string.search_ads_low_organic_title, element.keyword)
    }

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_ads_low_organic_title
    }
}
