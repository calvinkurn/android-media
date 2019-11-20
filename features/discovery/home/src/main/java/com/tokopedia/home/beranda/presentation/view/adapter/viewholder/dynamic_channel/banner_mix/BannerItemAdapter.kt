package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder.ProductItemViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder.SeeMoreBannerMixViewHolder
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import kotlinx.android.synthetic.main.thematic_card_view.view.*

class BannerItemAdapter(val bannerMixTypeFactoryImpl: BannerMixTypeFactoryImpl,
                        val layoutType: String,
                        val grids: Array<DynamicHomeChannel.Grid>,
                        val channel: DynamicHomeChannel.Channels,
                        val homeCategoryListener: HomeCategoryListener,
                        val visitables: List<Visitable<BannerMixTypeFactory>>,
                        val maxHeight: Int): BaseAdapter<BannerMixTypeFactoryImpl>(bannerMixTypeFactoryImpl, visitables){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        if (viewType == SeeMoreBannerMixViewHolder.LAYOUT_SEE_MORE) {
            val cardSeeMoreBanner = view.findViewById<CardView>(R.id.card_see_more_banner_mix)
            val layoutParams = cardSeeMoreBanner.layoutParams
            layoutParams.height = maxHeight
            cardSeeMoreBanner.layoutParams = layoutParams
        } else if(viewType == ProductItemViewHolder.LAYOUT_ITEM_CAROUSEL ||
                viewType == ProductItemViewHolder.LAYOUT_ITEM) {
            val cardThematic = view.findViewById<ThematicCardView>(R.id.banner_item)
            val cardProduct = cardThematic.cardViewProductCard
            val layoutParams = cardProduct.layoutParams
            layoutParams.height = maxHeight
            cardProduct.layoutParams = layoutParams
        }

        return bannerMixTypeFactoryImpl.createViewHolder(view, viewType)
    }
}