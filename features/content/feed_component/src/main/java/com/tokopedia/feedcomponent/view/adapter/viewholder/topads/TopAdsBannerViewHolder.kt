package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.unifycomponents.ImageUnify

class TopAdsBannerViewHolder(
    view: View,
    private val topAdsBannerListener: TopAdsBannerListener?,
    private val cardTitleListener: CardTitleView.CardTitleListener?
) : AbstractViewHolder<TopAdsBannerModel>(view) {
    private var topAdsBannerModel: TopAdsBannerModel? = null

    private val topAdsImageView: TopAdsImageView = view.findViewById(R.id.top_ads_banner)
    private val shimmerView: ImageUnify = view.findViewById(R.id.shimmer_view)
    private val cardTitle: CardTitleView = view.findViewById(R.id.cardTitle)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.feed_item_topads_banner
    }

    init {
        topAdsImageView.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                topAdsBannerModel?.run {
                    topAdsBannerList = imageDataList
                    if (topAdsBannerList.isNotEmpty()) {
                        bindTopAdsBanner(topAdsBannerList.first())
                    }

                }
            }

            override fun onError(t: Throwable) {

            }

        })
    }

    private fun bindTopAdsBanner(topAdsBanner: TopAdsImageViewModel) {
        topAdsImageView.loadImage(topAdsBanner)
        topAdsImageView.setTopAdsImageViewImpression(object :
            TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                topAdsBannerListener?.onTopAdsViewImpression(
                    topAdsBanner.bannerId
                        ?: "", topAdsBanner.imageUrl ?: ""
                )
            }
        })
        shimmerView.hide()
        topAdsImageView.show()
    }

    override fun bind(element: TopAdsBannerModel?) {
        topAdsBannerModel = element
        element?.run {
            if (topAdsBannerList.isNotEmpty()) {
                bindTopAdsBanner(topAdsBannerList.first())
            } else {
                shimmerView.show()
                topAdsImageView.getImageData(
                    "8",
                    1,
                    3,
                    "",
                    "",
                    ""
                )
            }
            if (title.text.isNotEmpty()) {
                cardTitle.visible()
                cardTitle.bind(title, template.cardbanner.title, adapterPosition)
                cardTitle.listener = cardTitleListener
            } else {
                cardTitle.gone()
            }
        }
    }

    interface TopAdsBannerListener {
        fun onTopAdsViewImpression(bannerId: String, imageUrl: String)
    }
}
