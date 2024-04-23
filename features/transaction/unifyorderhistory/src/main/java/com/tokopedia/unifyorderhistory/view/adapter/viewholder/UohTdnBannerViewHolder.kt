package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.v2.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.v2.tdnbanner.listener.TopAdsImageViewImpressionListener
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohTdnBannerLayoutBinding
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_RADIUS

class UohTdnBannerViewHolder(private val binding: UohTdnBannerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UohTypeData) {
        if (item.dataObject is TopAdsImageUiModel) {
            binding.uohTdnBanner.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                override fun onTopAdsImageViewClicked(applink: String?) {
                    RouteManager.route(itemView.context, applink)
                }
            })

            binding.uohTdnBanner.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
                override fun onTopAdsImageViewImpression(viewUrl: String) {
                    ImpresionTask(this@UohTdnBannerViewHolder.javaClass.canonicalName).execute(viewUrl)
                }
            })

            binding.uohTdnBanner.loadImage(item.dataObject, TDN_RADIUS)
        }
    }
}
