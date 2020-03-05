package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.ProductPdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.PdpViewListener
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView

class ProductPdpViewHolder (view: View, private val listener: PdpViewListener): AbstractViewHolder<ProductPdpDataModel>(view){
    private val productCardView: ThematicCardView? by lazy { view.findViewById<ThematicCardView>(R.id.banner_item) }
    override fun bind(element: ProductPdpDataModel) {
        productCardView?.run {
            val gridItem = element.grid
            setItemWithWrapBlankSpaceConfig(gridItem, element.blankSpaceConfig)
            setOnClickListener {
                listener.onSetTrackerProductClick(adapterPosition)
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel
    }
}