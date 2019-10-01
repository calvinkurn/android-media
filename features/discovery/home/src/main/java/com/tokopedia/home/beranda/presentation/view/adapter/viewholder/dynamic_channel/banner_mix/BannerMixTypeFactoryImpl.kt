package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix

import android.content.res.Resources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener

class BannerMixTypeFactoryImpl(val homeCategoryListener: HomeCategoryListener) : BaseAdapterTypeFactory(), BannerMixTypeFactory{

    override fun type(productBannerMixDataModel: ProductBannerMixDataModel): Int {
        return if (productBannerMixDataModel.channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC) {
            ProductItemViewHolder.LAYOUT_ITEM
        } else {
            ProductItemViewHolder.LAYOUT_ITEM_CAROUSEL
        }
    }

    override fun type(seeMoreBannerMixDataModel: SeeMoreBannerMixDataModel): Int {
        return SeeMoreBannerMixViewHolder.LAYOUT_SEE_MORE
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
//        var productCarouselWidth = parent.width
//        if (type == ProductItemViewHolder.LAYOUT_ITEM_CAROUSEL) {
//            val viewPortWidth = Resources.getSystem().displayMetrics.widthPixels
//            val marginBetweenEachProductCard = parent.context.resources.getDimensionPixelSize(R.dimen.dp_8)
//            val numItem = 2.75
//            productCarouselWidth = ((viewPortWidth*(1/numItem)) - marginBetweenEachProductCard).toInt()
//        }

        return when(type){
            ProductItemViewHolder.LAYOUT_ITEM, ProductItemViewHolder.LAYOUT_ITEM_CAROUSEL -> ProductItemViewHolder(
                    parent,
                    homeCategoryListener
            )
            SeeMoreBannerMixViewHolder.LAYOUT_SEE_MORE -> SeeMoreBannerMixViewHolder(
                    parent,
                    homeCategoryListener
            )
            else -> super.createViewHolder(parent, type)
        } as AbstractViewHolder<out Visitable<*>>
    }
}