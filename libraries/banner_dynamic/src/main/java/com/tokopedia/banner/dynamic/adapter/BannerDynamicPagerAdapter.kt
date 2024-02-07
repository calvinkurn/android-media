package com.tokopedia.banner.dynamic.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.cardview.widget.CardView
import com.bumptech.glide.request.target.Target
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.banner.dynamic.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy

/**
 * @author by furqan on 13/09/2019
 */
class BannerDynamicPagerAdapter(bannerImageUrls: List<String>,
                                onPromoClickListener: BannerView.OnPromoClickListener)
    : BannerViewPagerAdapter(bannerImageUrls, onPromoClickListener) {

    private lateinit var bannerCard: CardView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.layout_slider_banner_dynamic, parent, false)
        bannerCard = itemView.findViewById(R.id.banner_card)

        val layoutParams = itemView.layoutParams

        if (parent.width != 0) {
            layoutParams.width = (parent.width * 0.9).toInt()
        } else {
            val mWinMgr = itemView.context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = mWinMgr.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x

            layoutParams.width = (width * 0.9).toInt()
        }

        itemView.layoutParams = layoutParams
        bannerCard.visibility = View.VISIBLE

        return BannerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (bannerImageUrls[position] != null && bannerImageUrls[position].isNotEmpty()) {
            holder.bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(position)
            )
        }
        try {
            holder.bannerImage.loadImage(bannerImageUrls[position]) {
                setCacheStrategy(MediaCacheStrategy.DATA)
                overrideSize(Resize(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                centerCrop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
