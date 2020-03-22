package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.ProductCardFlashSaleView

class EmptyCardViewHolder (view: View, private val listener: FlashSaleCardListener): AbstractViewHolder<EmptyDataModel>(view){

    override fun bind(element: EmptyDataModel?) {

    }


    companion object{
        val LAYOUT = R.layout.home_banner_item_empty_carousel
    }
}