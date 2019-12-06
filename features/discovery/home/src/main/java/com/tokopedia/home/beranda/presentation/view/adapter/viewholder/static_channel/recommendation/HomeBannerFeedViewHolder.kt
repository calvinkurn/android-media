package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerFeedViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import timber.log.Timber

class HomeBannerFeedViewHolder(itemView: View, private val homeFeedview: HomeFeedContract.View) : AbstractViewHolder<BannerFeedViewModel>(itemView) {

    private val context: Context by lazy { itemView.context }

    private val bannerImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.bannerImageView) }

    override fun bind(element: BannerFeedViewModel) {
        bannerImageView.setOnClickListener {
            HomePageTracking.eventClickOnBannerFeed(
                    context, element, homeFeedview.getTabName()
            )
            RouteManager.route(context, element.applink)
        }

        Glide.with(context)
                .asBitmap()
                .load(element.imageUrl)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?,
                                              target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        ImageHandler.logError(context, e, element.imageUrl);
                        return false;
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                 target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        return false;
                    }
                })
                .into(bannerImageView)

        bannerImageView.addOnImpressionListener(element,
                object : ViewHintListener{
                    override fun onViewHint() {
                        HomePageTracking.eventImpressionOnBannerFeed(
                            homeFeedview.getTrackingQueue(),
                            element,
                            homeFeedview.getTabName())
                    }

                })
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.home_feed_banner
    }
}
