package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxTopadsVerticalBannerItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.EIGHT_DP
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsVerticalBannerUiModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.v2.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.v2.tdnvertical.listener.TdnVerticalBannerResponseListener
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxTopAdsVerticalBannerViewHolder(
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
        val ads = topAdsUiModel?.ads ?: listOf()
        binding?.inboxVerticalTopadsBanner?.renderTdnBanner(ads, EIGHT_DP, ::onTdnBannerClicked)
    }

    private fun bindTopAds(uiModel: UniversalInboxTopAdsVerticalBannerUiModel) {
        topAdsUiModel = uiModel
        if (!uiModel.isRequested){
            binding?.inboxVerticalTopadsBanner?.getTdnData(
                SOURCE,
                ADS_COUNT,
                dimenId = DIMEN_ID
            )
            topAdsUiModel?.isRequested = true
        }
    }

    private fun bindListener() {
        binding?.inboxVerticalTopadsBanner?.setTdnResponseListener(this)
    }

    private fun onTdnBannerClicked(bannerData: TopAdsImageUiModel) {
        topAdsClickListener.onTopAdsImageViewClicked(bannerData.applink)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_topads_vertical_banner_item

        const val SOURCE = "28"
        const val ADS_COUNT = 1
        const val DIMEN_ID = 17
    }

    override fun onTdnVerticalBannerResponse(data: ArrayList<TopAdsImageUiModel>) {
        topAdsUiModel?.ads = data
        bindBanner()
    }

    override fun onError(t: Throwable) {

    }
}
