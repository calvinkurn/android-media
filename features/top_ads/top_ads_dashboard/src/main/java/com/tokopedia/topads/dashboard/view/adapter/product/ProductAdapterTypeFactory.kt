package com.tokopedia.topads.dashboard.view.adapter.product

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemModel


interface ProductAdapterTypeFactory {

    fun type(model: ProductItemModel): Int

    fun type(model: ProductEmptyModel): Int

    fun holder(type: Int, view: View): ProductViewHolder<*>

}