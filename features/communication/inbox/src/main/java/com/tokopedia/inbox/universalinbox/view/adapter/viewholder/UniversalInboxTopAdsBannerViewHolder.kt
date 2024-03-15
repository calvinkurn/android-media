package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxTopadsBannerItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.EIGHT_DP
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxTopAdsBannerViewHolder constructor(
    itemView: View,
    private val tdnBannerResponseListener: TdnBannerResponseListener,
    private val topAdsClickListener: TopAdsImageViewClickListener
) : AbstractViewHolder<UniversalInboxTopAdsBannerUiModel>(itemView) {

    private val binding: UniversalInboxTopadsBannerItemBinding? by viewBinding()

    override fun bind(uiModel: UniversalInboxTopAdsBannerUiModel) {
        bindListener()
        bindTopAds(uiModel)
        bindTdnBanner(uiModel)
    }

    private fun bindTopAds(uiModel: UniversalInboxTopAdsBannerUiModel) {
        if (!uiModel.hasAds() && !uiModel.requested) {
            uiModel.requested = true
            binding?.inboxTopadsBanner?.getTdnData(
                SOURCE,
                adsCount = ADS_COUNT,
                dimenId = DIMEN_ID
            )
        }
    }

    private fun bindTdnBanner(uiModel: UniversalInboxTopAdsBannerUiModel) {
        uiModel.ads?.let {
            binding?.inboxTopadsBanner?.renderTdnBanner(it, EIGHT_DP, ::onTdnBannerClicked)
        }
    }

    private fun bindListener() {
        binding?.inboxTopadsBanner?.setTdnResponseListener(tdnBannerResponseListener)
    }

    private fun onTdnBannerClicked(bannerData: TopAdsImageUiModel) {
        topAdsClickListener.onTopAdsImageViewClicked(bannerData.applink)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_topads_banner_item

        const val SOURCE = "5"
        const val ADS_COUNT = 4
        const val DIMEN_ID = 3
    }
}
