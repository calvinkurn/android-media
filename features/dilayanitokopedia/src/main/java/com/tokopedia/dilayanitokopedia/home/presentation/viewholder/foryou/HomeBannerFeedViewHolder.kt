package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.BannerRecommendationDataModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeBannerFeedViewHolder(itemView: View) : SmartAbstractViewHolder<BannerRecommendationDataModel>(itemView) {

    private val context: Context by lazy { itemView.context }

//    private val bannerImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.bannerImageView) }

    override fun bind(element: BannerRecommendationDataModel, listener: SmartListener) {
//        bannerImageView.setOnClickListener {
//            HomePageTracking.eventClickOnBannerFeed(
//                element, element.tabName
//            )
//            RouteManager.route(context, element.applink)
//        }
//
//        Glide.with(context)
//            .asBitmap()
//            .load(element.imageUrl)
//            .dontAnimate()
//            .placeholder(R.drawable.loading_page)
//            .error(R.drawable.error_drawable)
//            .into(bannerImageView)
//
//        bannerImageView.addOnImpressionListener(element,
//            object : ViewHintListener {
//                override fun onViewHint() {
//                    (listener as HomeRecommendationListener).onBannerImpression(element)
//                }
//
//            })
    }

    companion object {

        //        val LAYOUT = R.layout.home_feed_banner
        @LayoutRes
        val LAYOUT = R.layout.loading_layout
    }
}
