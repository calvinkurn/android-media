package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_account.AccountConstants.TDNBanner.TDN_RADIUS
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.TdnBannerLayoutBinding
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.utils.view.binding.viewBinding

class TdnBannerViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val binding: TdnBannerLayoutBinding? by viewBinding()

    fun bind(element: TopAdsImageViewModel) {
        binding?.accountHomeTdnBanner?.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                RouteManager.route(itemView.context, applink)
            }

        })

        binding?.accountHomeTdnBanner?.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                ImpresionTask(this@TdnBannerViewHolder.javaClass.canonicalName).execute(viewUrl)
            }
        })
        binding?.accountHomeTdnBanner?.loadImage(element, TDN_RADIUS)
    }

    companion object {
        val LAYOUT = R.layout.tdn_banner_layout
    }
}
