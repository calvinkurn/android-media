package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.ProductBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder.ProductItemViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.SeeMoreBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder.SeeMoreBannerMixViewHolder

class BannerMixTypeFactoryImpl(val homeCategoryListener: HomeCategoryListener) : BaseAdapterTypeFactory(), BannerMixTypeFactory {

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