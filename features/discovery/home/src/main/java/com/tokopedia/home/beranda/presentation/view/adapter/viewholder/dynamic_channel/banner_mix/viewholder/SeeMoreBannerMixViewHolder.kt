package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.SeeMoreBannerMixDataModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.unifyprinciples.Typography

class SeeMoreBannerMixViewHolder(view: View,
                                 val homeCategoryListener: HomeCategoryListener): AbstractViewHolder<SeeMoreBannerMixDataModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT_SEE_MORE = R.layout.home_banner_item_carousel_see_more
    }
    private val bannerMixArrow: ImageView by lazy { view.findViewById<ImageView>(R.id.banner_mix_arrow) }
    private val container: View by lazy { view.findViewById<View>(R.id.container_banner_mix_more) }
    private val bannerBackgroundImage: ImageView by lazy { view.findViewById<ImageView>(R.id.background_banner_mix_more)}
    private val seeMoreText: Typography by lazy { view.findViewById<Typography>(R.id.tv_dc_mix_see_more)}

    override fun bind(seeMoreBannerMixDataModel: SeeMoreBannerMixDataModel) {
        val channel = seeMoreBannerMixDataModel.channel
        bannerBackgroundImage.setOnClickListener {
            homeCategoryListener.onSectionItemClicked(channel.header.applink)
        }
        Glide.with(itemView.context)
                .load(channel.header.backImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(bannerBackgroundImage)
        container.setOnClickListener {
            HomePageTracking.eventClickSeeAllBannerMixChannel(channel.id, channel.header.name)
        }
    }

    val productCard: ProductCardViewSmallGrid by lazy { view.findViewById<ProductCardViewSmallGrid>(R.id.banner_item) }
}