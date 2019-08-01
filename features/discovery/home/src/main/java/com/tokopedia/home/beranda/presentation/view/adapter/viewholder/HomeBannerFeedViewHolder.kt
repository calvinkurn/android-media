package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.gql.feed.Badge
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.viewmodel.BannerFeedViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardView
import com.tokopedia.topads.sdk.view.ImpressedImageView

import java.util.ArrayList

class HomeBannerFeedViewHolder(itemView: View, private val homeFeedview: HomeFeedContract.View) : AbstractViewHolder<BannerFeedViewModel>(itemView) {

    private val context: Context

    private val bannerImageView: ImageView

    init {
        this.context = itemView.context
        bannerImageView = itemView.findViewById(R.id.bannerImageView)
    }

    override fun bind(element: BannerFeedViewModel) {
        bannerImageView.setOnClickListener {
            HomePageTracking.eventClickOnBannerFeed(
                    context, element, homeFeedview.tabName
            )
            RouteManager.route(context, element.applink)
        }

        Glide.with(context)
                .load(element.imageUrl)
                .asBitmap()
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .into(bannerImageView)

        bannerImageView.addOnImpressionListener(element,
                object : ViewHintListener{
                    override fun onViewHint() {
                        HomePageTracking.eventImpressionOnBannerFeed(
                            homeFeedview.trackingQueue,
                            element,
                            homeFeedview.tabName)
                    }

                })
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.home_feed_banner
    }
}
