package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import kotlinx.android.synthetic.main.item_home_banner_topads_layout.view.*

class HomeRecommendationBannerTopAdsViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationBannerTopAdsDataModel>(view){
    companion object{
        val LAYOUT = R.layout.item_home_banner_topads_layout
    }

    override fun bind(element: HomeRecommendationBannerTopAdsDataModel, listener: SmartListener) {
        loadImageTopAds(element, listener as HomeRecommendationListener)
        itemView.home_recom_topads_image_view?.setOnClickListener {
            listener.onBannerTopAdsClick(element, adapterPosition)
        }
    }


    private fun loadImageTopAds(recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsDataModel, listener: HomeRecommendationListener){
        itemView.home_recom_topads_loader_image?.show()
        itemView.home_recom_topads_loader_image?.hide()
        Glide.with(itemView.context)
                .load(recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageUrl)
                .override(itemView.context.resources.displayMetrics.widthPixels,
                        getHeight(recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageWidth, recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageHeight))
                .fitCenter()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        itemView.home_recom_topads_image_view?.hide()
                        itemView.home_recom_topads_loader_image?.hide()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        itemView.home_recom_topads_image_view?.show()
                        itemView.home_recom_topads_loader_image?.hide()
                        listener.onBannerTopAdsImpress(recommendationBannerTopAdsDataModelDataModel, adapterPosition)
                        return false
                    }
                })
                .into(itemView.home_recom_topads_image_view)
    }

    private fun getHeight(width: Int, height: Int): Int {
        val metrics = itemView.context.resources.displayMetrics
        val deviceWidth = metrics.widthPixels.toFloat()
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }
}
