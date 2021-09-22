package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_account.AccountConstants.TDNBanner.TDN_RADIUS
import com.tokopedia.home_account.R
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class TdnBannerViewHolder(itemView: View) : BaseViewHolder(itemView) {

    fun bind(element: TopAdsImageViewModel) {
        with(itemView) {
            val tdnBanner = this.findViewById<TopAdsImageView>(R.id.accountHomeTdnBanner)
            tdnBanner.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                override fun onTopAdsImageViewClicked(applink: String?) {
                    RouteManager.route(itemView.context, applink)
                }

            })
            tdnBanner.loadImage(element, TDN_RADIUS)
        }
    }

    companion object {
        val LAYOUT = R.layout.tdn_banner_layout
    }
}
