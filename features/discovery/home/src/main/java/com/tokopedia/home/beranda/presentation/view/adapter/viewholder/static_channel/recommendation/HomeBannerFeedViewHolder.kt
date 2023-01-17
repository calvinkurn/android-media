package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeBannerFeedViewHolder(itemView: View) : SmartAbstractViewHolder<BannerRecommendationDataModel>(itemView) {

    private val context: Context by lazy { itemView.context }

    private val bannerImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.bannerImageView) }

    override fun bind(element: BannerRecommendationDataModel, listener: SmartListener) {
        bannerImageView.setOnClickListener {
            HomePageTracking.eventClickOnBannerFeed(
                element,
                element.tabName
            )
            RouteManager.route(context, element.applink)
        }

        Glide.with(context)
            .asBitmap()
            .load(element.imageUrl)
            .dontAnimate()
            .placeholder(R.drawable.loading_page)
            .error(R.drawable.error_drawable)
            .into(bannerImageView)

        bannerImageView.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    (listener as HomeRecommendationListener).onBannerImpression(element)
                }
            }
        )
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.home_feed_banner
    }
}
