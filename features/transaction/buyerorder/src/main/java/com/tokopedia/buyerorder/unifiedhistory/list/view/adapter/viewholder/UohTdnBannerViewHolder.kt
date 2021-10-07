package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TDN_RADIUS
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class UohTdnBannerViewHolder(itemView: View) :
        UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    private val tdnBanner: TopAdsImageView by lazy {
        itemView.findViewById<TopAdsImageView>(
                R.id.uohTdnBanner
        )
    }

    private fun bindTdn(element: TopAdsImageViewModel) {

        tdnBanner.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                RouteManager.route(itemView.context, applink)
            }
        })

        tdnBanner.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                ImpresionTask(this@UohTdnBannerViewHolder.javaClass.canonicalName).execute(viewUrl)
            }
        })

        tdnBanner.loadImage(element, TDN_RADIUS)
    }


    override fun bind(item: UohTypeData, position: Int) {
        bindTdn(item.dataObject as TopAdsImageViewModel)
    }
}
