package com.tokopedia.home.account.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.TdnBannerViewModel
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class TdnBannerViewHolder(itemView: View) : AbstractViewHolder<TdnBannerViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.old_account_tdn_banner_layout
    }

    override fun bind(element: TdnBannerViewModel) {
        with(itemView) {
            val tdnBanner = this.findViewById<TopAdsImageView>(com.tokopedia.home_account.R.id.accountHomeTdnBanner)
            tdnBanner.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                override fun onTopAdsImageViewClicked(applink: String?) {
                    RouteManager.route(itemView.context, applink)
                }

            })

            tdnBanner.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener{
                override fun onTopAdsImageViewImpression(viewUrl: String) {
                    ImpresionTask(this@TdnBannerViewHolder.javaClass.canonicalName).execute(viewUrl)
                }
            })
            tdnBanner.loadImage(element.topAdsImageViewModel, AccountConstants.TDNBanner.TDN_RADIUS)
        }
    }
}
