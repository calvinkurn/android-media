package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopProductDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopSeeMoreDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.viewHolder.MixTopProductViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.viewHolder.MixTopSeeMoreViewHolder

interface MixTopTypeFactory{
    fun type(productBannerMixDataModel: MixTopProductDataModel) : Int
    fun type(seeMoreBannerMixDataModel: MixTopSeeMoreDataModel) : Int
    fun onCreateViewHolder(parent: View, type: Int) : AbstractViewHolder<MixTopVisitable>
}

class MixTopTypeFactoryImpl(val homeCategoryListener: HomeCategoryListener) : MixTopTypeFactory{
    override fun type(productBannerMixDataModel: MixTopProductDataModel): Int {
        return MixTopProductViewHolder.LAYOUT_ITEM
    }

    override fun type(seeMoreBannerMixDataModel: MixTopSeeMoreDataModel): Int {
        return MixTopSeeMoreViewHolder.LAYOUT_SEE_MORE
    }

    override fun onCreateViewHolder(parent: View, type: Int): AbstractViewHolder<MixTopVisitable> {
        return when(type){
            MixTopSeeMoreViewHolder.LAYOUT_SEE_MORE -> MixTopSeeMoreViewHolder(
                    parent,
                    homeCategoryListener
            )
            else -> MixTopProductViewHolder(
                    parent,
                    homeCategoryListener
            )
        } as AbstractViewHolder<MixTopVisitable>
    }
}