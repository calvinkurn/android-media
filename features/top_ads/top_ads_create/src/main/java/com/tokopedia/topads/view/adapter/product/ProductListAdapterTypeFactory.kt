package com.tokopedia.topads.view.adapter.product

import android.view.View
import com.tokopedia.topads.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */
interface ProductListAdapterTypeFactory {

    fun type(model: ProductItemViewModel): Int

    fun type(model: ProductEmptyViewModel): Int

    fun type(model: ProductShimmerViewModel): Int

    fun holder(type: Int, view: View): ProductViewHolder<*>

}