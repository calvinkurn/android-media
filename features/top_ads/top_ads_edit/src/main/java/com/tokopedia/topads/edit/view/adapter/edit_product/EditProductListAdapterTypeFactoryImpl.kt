package com.tokopedia.topads.edit.view.adapter.edit_product

import android.view.View
import com.tokopedia.topads.edit.view.adapter.edit_product.viewholder.EditProductEmptyViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_product.viewholder.EditProductItemViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_product.viewholder.EditProductShimmerViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_product.viewholder.EditProductViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.*


/**
 * Created by Pika on 8/4/20.
 */

class EditProductListAdapterTypeFactoryImpl(var actionSelected: ((pos:Int) -> Unit)?) : EditProductListAdapterTypeFactory {

    override fun type(model: EditProductItemViewModel): Int = EditProductItemViewHolder.LAYOUT

    override fun type(model: EditProductEmptyViewModel): Int = EditProductEmptyViewHolder.LAYOUT

    override fun type(model: EditProductShimmerViewModel): Int = EditProductShimmerViewHolder.LAYOUT

    override fun holder(type: Int, view: View): EditProductViewHolder<*> {
        return when(type){
            EditProductItemViewHolder.LAYOUT -> EditProductItemViewHolder(view, actionSelected)
            EditProductEmptyViewHolder.LAYOUT -> EditProductEmptyViewHolder(view)
            EditProductShimmerViewHolder.LAYOUT -> EditProductShimmerViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}