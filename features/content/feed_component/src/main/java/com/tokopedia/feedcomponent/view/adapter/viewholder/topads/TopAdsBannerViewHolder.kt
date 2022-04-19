package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import kotlinx.android.synthetic.main.feed_item_topads_banner.view.*

class TopAdsBannerViewHolder(view: View,
                             private val topAdsBannerListener: TopAdsBannerListener?,
                             private val cardTitleListener: CardTitleView.CardTitleListener?)
    : AbstractViewHolder<TopAdsBannerViewModel>(view) {
    private var topAdsBannerViewModel: TopAdsBannerViewModel? = null

    private val topAdsImageView: TopAdsImageView = view.findViewById(R.id.top_ads_banner)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.feed_item_topads_banner
    }

    init {
        topAdsImageView.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                topAdsBannerViewModel?.run {
                    topAdsBannerList = imageDataList
                    if (!topAdsBannerList.isNullOrEmpty()) {
                        bindTopAdsBanner(topAdsBannerList.first())
                    }

                }
            }

            override fun onError(t: Throwable) {

            }

        })
    }

    private fun bindTopAdsBanner(topAdsBanner: TopAdsImageViewModel) {
        itemView.top_ads_banner.loadImage(topAdsBanner)
        itemView.top_ads_banner.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                topAdsBannerListener?.onTopAdsViewImpression(topAdsBanner.bannerId
                        ?: "", topAdsBanner.imageUrl ?: "")
            }
        })
        itemView.shimmer_view.hide()
        itemView.top_ads_banner.show()
    }

    override fun bind(element: TopAdsBannerViewModel?) {
        topAdsBannerViewModel = element
        element?.run {
            if (!topAdsBannerList.isNullOrEmpty()) {
                bindTopAdsBanner(topAdsBannerList.first())
            } else {
                itemView.shimmer_view.show()
                topAdsImageView.getImageData("8",
                        1,
                        3,
                        "",
                        "",
                        "")
            }
            if (title.text.isNotEmpty()) {
                itemView.cardTitle.visibility = View.VISIBLE
                itemView.cardTitle.bind(title, template.cardbanner.title, adapterPosition)
                itemView.cardTitle.listener = cardTitleListener
            } else {
                itemView.cardTitle.visibility = View.GONE
            }
        }
    }

    interface TopAdsBannerListener {
        fun onTopAdsViewImpression(bannerId: String, imageUrl: String)
    }
}