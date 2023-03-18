package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.BannerRecommendationDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeBannerFeedViewHolder(itemView: View) : SmartAbstractViewHolder<BannerRecommendationDataModel>(itemView) {

    private val context: Context by lazy { itemView.context }

    private val bannerImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.bannerImageView) }

    override fun bind(element: BannerRecommendationDataModel, listener: SmartListener) {
        bannerImageView.setOnClickListener {
            RouteManager.route(context, element.applink)
        }

        Glide.with(context)
            .asBitmap()
            .load(element.imageUrl)
            .dontAnimate()
            .placeholder(R.drawable.dt_loading_page)
            .error(R.drawable.dt_loading_page)
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
        val LAYOUT = R.layout.dt_home_feed_banner
    }
}
