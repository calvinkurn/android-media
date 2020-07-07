package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.BannerAdsTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import kotlinx.android.synthetic.main.home_dc_topads_banner.view.*

class TopadsBannerViewHolder(val view: View, val categoryListener: HomeCategoryListener) :
        DynamicChannelViewHolder(view, categoryListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_topads_banner
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        view.home_topads_image_view.setApiResponseListener(object: TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                if (imageDataList.size > 0) {
                    view.home_topads_image_view.loadImage(imageDataList[0])
                } else {
                    onError(Throwable())
                }
            }

            override fun onError(t: Throwable) {
                categoryListener.removeViewHolderAtPosition(adapterPosition)
            }
        })

        view.home_topads_image_view.setTopAdsImageViewImpression(object: TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                BannerAdsTracking.sendBannerAdsImpressionTracking(
                        categoryListener.getTrackingQueueObj(),
                        channel,
                        categoryListener.userId,
                        adapterPosition
                )
            }
        })

        view.home_topads_image_view.setTopAdsImageViewClick(object: TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                BannerAdsTracking.sendBannerAdsClickTracking(
                        channel,
                        categoryListener.userId,
                        adapterPosition
                )
            }
        })

        view.home_topads_image_view.getImageData(
                "1",
                1,
                3
        )
    }

    override fun getViewHolderClassName(): String {
        return TopadsBannerViewHolder::class.java.toString()
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {

    }
}