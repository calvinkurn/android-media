package com.tokopedia.home_wishlist.view.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.BannerTopAdsDataModel
import com.tokopedia.home_wishlist.model.datamodel.LoadMoreDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlinx.android.synthetic.main.layout_banner_topads.view.*
import timber.log.Timber

class BannerTopAdsViewHolder (view: View) : SmartAbstractViewHolder<BannerTopAdsDataModel>(view){
    override fun bind(element: BannerTopAdsDataModel, listener: SmartListener) {
        loadImageTopAds(element.topAdsDataModel)
        itemView.wishlist_topads_image_view.setOnClickListener {
            Timber.d("TopAdsImageView is clicked")
            RouteManager.route(itemView.context, element.topAdsDataModel.applink)
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                    this::class.java.simpleName,
                    element.topAdsDataModel.adClickUrl,
                    "",
                    "",
                    ""
            )
        }
    }

    private fun loadImageTopAds(topAdsImageViewModel: TopAdsImageViewModel){
        itemView.wishlist_topads_loader_image.show()
        itemView.wishlist_topads_image_view.hide()
        Glide.with(itemView.context)
                .load(topAdsImageViewModel.imageUrl)
                .override(itemView.context.resources.displayMetrics.widthPixels,
                        getHeight(topAdsImageViewModel.imageWidth, topAdsImageViewModel.imageHeight))
                .fitCenter()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        itemView.wishlist_topads_image_view.hide()
                        itemView.wishlist_topads_loader_image.hide()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                                this::class.java.simpleName,
                                topAdsImageViewModel.adViewUrl,
                                "",
                                "",
                                ""
                        )
                        itemView.wishlist_topads_image_view.show()
                        itemView.wishlist_topads_loader_image.hide()
                        return false

                    }
                })
                .into(itemView.wishlist_topads_image_view)
    }

    private fun getHeight(width: Int, height: Int): Int {
        val metrics = itemView.context.resources.displayMetrics
        val deviceWidth = metrics.widthPixels.toFloat()
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }

    companion object{
        val LAYOUT = R.layout.layout_banner_topads
    }
}