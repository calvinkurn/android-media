package com.tokopedia.home_wishlist.view.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.BannerTopAdsDataModel
import com.tokopedia.home_wishlist.view.listener.TopAdsListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import kotlinx.android.synthetic.main.layout_banner_topads.view.*

class BannerTopAdsViewHolder (view: View) : SmartAbstractViewHolder<BannerTopAdsDataModel>(view){
    override fun bind(element: BannerTopAdsDataModel, listener: SmartListener) {
        if(listener is TopAdsListener) {
            loadImageTopAds(element, listener)


            itemView.wishlist_topads_image_view.setOnClickListener {

                // to prevent ArrayIndexOutOfBoundsException
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onBannerTopAdsClick(element, adapterPosition)
                }
            }
        }
    }

    private fun loadImageTopAds(bannerTopAdsDataModel: BannerTopAdsDataModel, listener: TopAdsListener){
        itemView.wishlist_topads_loader_image.show()
        itemView.wishlist_topads_image_view.hide()
        listener.onBannerTopAdsImpress(bannerTopAdsDataModel, adapterPosition)
        Glide.with(itemView.context)
                .load(bannerTopAdsDataModel.topAdsDataModel.imageUrl)
                .override(itemView.context.resources.displayMetrics.widthPixels,
                        getHeight(bannerTopAdsDataModel.topAdsDataModel.imageWidth, bannerTopAdsDataModel.topAdsDataModel.imageHeight))
                .fitCenter()
                .transform(RoundedCorners(24))
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        itemView.wishlist_topads_image_view.hide()
                        itemView.wishlist_topads_loader_image.hide()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
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