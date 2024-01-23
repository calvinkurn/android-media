package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxTopadsVerticalBannerItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.EIGHT_DP
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsVerticalBannerUiModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnVerticalBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxTopAdsVerticalBannerViewHolder constructor(
    itemView: View,
    private val topAdsClickListener: TopAdsImageViewClickListener
) : AbstractViewHolder<UniversalInboxTopAdsVerticalBannerUiModel>(itemView), TdnVerticalBannerResponseListener {

    private val binding: UniversalInboxTopadsVerticalBannerItemBinding? by viewBinding()
    private var topAdsUiModel:UniversalInboxTopAdsVerticalBannerUiModel? = null

    override fun bind(uiModel: UniversalInboxTopAdsVerticalBannerUiModel) {
        bindListener()
        bindTopAds(uiModel)
        bindBanner()
    }

    private fun bindBanner() {
        binding?.inboxVerticalTopadsBanner?.renderTdnBanner(topAdsUiModel?.ads!!, EIGHT_DP, ::onTdnBannerClicked)
    }

    private fun bindTopAds(uiModel: UniversalInboxTopAdsVerticalBannerUiModel) {
        topAdsUiModel = uiModel
        if (!topAdsUiModel!!.isRequested){
            binding?.inboxVerticalTopadsBanner?.getTdnData(
                SOURCE,
                ADS_COUNT,
                dimenId = DIMEN_ID
            )
            topAdsUiModel!!.isRequested = true
        }
    }

    private fun bindListener() {
        binding?.inboxVerticalTopadsBanner?.setTdnResponseListener(this)
    }

    private fun onTdnBannerClicked(bannerData: TopAdsImageViewModel) {
        topAdsClickListener.onTopAdsImageViewClicked(bannerData.applink)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_topads_vertical_banner_item

        const val SOURCE = "28"
        const val ADS_COUNT = 1
        const val DIMEN_ID = 17
    }

    override fun onTdnVerticalBannerResponse(data: ArrayList<TopAdsImageViewModel>) {
        topAdsUiModel?.ads = data
        bindBanner()
    }

    override fun onError(t: Throwable) {

    }
}
