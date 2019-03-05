package com.tokopedia.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView

/**
 * Created by meta on 28/02/19.
 * Credit Devara Fikry
 */

class BannerAdapter(bannerImageUrls : List<String>,
                    onPromoClickListener : BannerView.OnPromoClickListener) :
        BannerPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.banner_item_layout, parent, false)

        val layoutParams = itemView.getLayoutParams()
        layoutParams.width = (parent.width * 0.9).toInt()
        itemView.setLayoutParams(layoutParams)
        itemView.banner_card.visibility = View.VISIBLE

        return BannerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (bannerImageUrls[position] != null && bannerImageUrls[position].length > 0) {
            holder.bannerImage.setOnClickListener(
                    getBannerImageOnClickListener(position)
            )
        }
        try {
            Glide.with(holder.itemView.context)
                    .load(bannerImageUrls[position])
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(com.tokopedia.design.R.drawable.ic_loading_image)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .centerCrop()
                    .into(holder.bannerImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

