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
import com.tokopedia.productcard.v2.BlankSpaceConfig
import kotlinx.android.synthetic.main.thematic_card_view.view.*

class BannerItemAdapter(var bannerMixTypeFactoryImpl: BannerMixTypeFactoryImpl,
                        val layoutType: String,
                        val grids: Array<DynamicHomeChannel.Grid>,
                        val channel: DynamicHomeChannel.Channels,
                        val homeCategoryListener: HomeCategoryListener,
                        val visitablesInitial: List<Visitable<BannerMixTypeFactory>>): BaseAdapter<BannerMixTypeFactoryImpl>(bannerMixTypeFactoryImpl, visitablesInitial){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return bannerMixTypeFactoryImpl.createViewHolder(view, viewType)
    }

    fun setItems(visitables: List<Visitable<*>>) {
        this.visitables = visitables
        notifyDataSetChanged()
    }
}