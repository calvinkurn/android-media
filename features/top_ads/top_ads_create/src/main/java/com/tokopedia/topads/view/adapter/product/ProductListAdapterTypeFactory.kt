package com.tokopedia.topads.view.adapter.product

import android.view.View
import com.tokopedia.topads.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.view.adapter.product.viewmodel.KeyWordItemUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerUiModel

/**
 * Author errysuprayogi on 11,November,2019
 */
interface ProductListAdapterTypeFactory {

    fun type(model: ProductItemUiModel): Int

    fun type(model: KeyWordItemUiModel): Int

    fun type(model: ProductEmptyUiModel): Int

    fun type(model: ProductShimmerUiModel): Int

    fun holder(type: Int, view: View): ProductViewHolder<*>

}
