package com.tokopedia.topads.edit.view.adapter.edit_product

import android.view.View
import com.tokopedia.topads.edit.view.adapter.edit_product.viewholder.EditProductViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductShimmerViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */
interface EditProductListAdapterTypeFactory {

    fun type(model: EditProductItemViewModel): Int

    fun type(model: EditProductEmptyViewModel): Int

    fun type(model: EditProductShimmerViewModel): Int

    fun holder(type: Int, view: View): EditProductViewHolder<*>

}