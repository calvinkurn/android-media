package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductOpenShopDataModel

class ProductOpenShopViewHolder(view: View): AbstractViewHolder<ProductOpenShopDataModel>(view){

    companion object{
        val LAYOUT = R.layout.item_dynamic_open_shop
    }
    override fun bind(element: ProductOpenShopDataModel?) {

    }
}