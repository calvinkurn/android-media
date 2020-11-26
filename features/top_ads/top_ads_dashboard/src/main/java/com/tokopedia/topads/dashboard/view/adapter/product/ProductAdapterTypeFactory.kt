package com.tokopedia.topads.dashboard.view.adapter.product

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemViewModel


interface ProductAdapterTypeFactory {

    fun type(model: ProductItemViewModel): Int

    fun type(model: ProductEmptyViewModel): Int

    fun holder(type: Int, view: View): ProductViewHolder<*>

}