package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductTradeinDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_trade_in.view.*

class ProductTradeinViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductTradeinDataModel>(view) {


    companion object {
        val LAYOUT = R.layout.item_dynamic_trade_in
    }

    override fun bind(element: ProductTradeinDataModel) {
        if (element.shouldRenderInitialData) {
            element.shouldRenderInitialData = false
            view.tv_trade_in.tradeInReceiver.checkTradeIn(element.tradeInParams, false)
        }

        view.setOnClickListener {
            listener.onTradeinClicked(element.tradeInParams)
        }
    }

}