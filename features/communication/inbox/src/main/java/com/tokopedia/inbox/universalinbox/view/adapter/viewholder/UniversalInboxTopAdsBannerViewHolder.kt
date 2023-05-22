package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxTopadsBannerItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.EIGHT_DP
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxTopAdsBannerViewHolder constructor(
    itemView: View,
    private val tdnBannerResponseListener: TdnBannerResponseListener,
    private val topAdsClickListener: TopAdsImageViewClickListener,
) : BaseViewHolder(itemView) {

    private val binding: UniversalInboxTopadsBannerItemBinding? by viewBinding()

    fun bind(uiModel: UniversalInboxTopAdsBannerUiModel) {
        bindTopAds(uiModel)
        bindTdnBanner(uiModel)
    }

    private fun bindTdnBanner(uiModel: UniversalInboxTopAdsBannerUiModel) {
        uiModel.ads?.let {
            binding?.inboxTopadsBanner?.renderTdnBanner(it, EIGHT_DP, ::onTdnBannerClicked)
        }
    }

    private fun bindTopAds(uiModel: UniversalInboxTopAdsBannerUiModel) {
        if (!uiModel.hasAds() && !uiModel.requested) {
            binding?.inboxTopadsBanner?.setTdnResponseListener(tdnBannerResponseListener)
            binding?.inboxTopadsBanner?.getTdnData(
                SOURCE,
                adsCount = ADS_COUNT,
                dimenId = DIMEN_ID
            )
            uiModel.requested = true
        }

    }

    private fun onTdnBannerClicked(applink: String) {
        topAdsClickListener.onTopAdsImageViewClicked(applink)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_topads_banner_item

        const val SOURCE = "5"
        const val ADS_COUNT = 4
        const val DIMEN_ID = 3
    }
}
