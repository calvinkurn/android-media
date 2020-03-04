package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.MixTopTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopProductDataModel
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView

class MixTopProductViewHolder(view: View, val homeCategoryListener: HomeCategoryListener): AbstractViewHolder<MixTopProductDataModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT_ITEM = R.layout.home_banner_item_carousel
    }
    private val productCardView: ThematicCardView? by lazy { view.findViewById<ThematicCardView>(R.id.banner_item) }

    override fun bind(mixTopProductDataModel: MixTopProductDataModel) {
        productCardView?.run {
            val gridItem = mixTopProductDataModel.grid
            setItemWithWrapBlankSpaceConfig(gridItem, mixTopProductDataModel.blankSpaceConfig)
            setOnClickListener {
                MixTopTracking.getMixTopClick(MixTopTracking.mapChannelToProductTracker(mixTopProductDataModel.channel), mixTopProductDataModel.channel.header.name, mixTopProductDataModel.positionOnWidgetHome)
                homeCategoryListener.onSectionItemClicked(gridItem.applink)
            }
        }
    }
}