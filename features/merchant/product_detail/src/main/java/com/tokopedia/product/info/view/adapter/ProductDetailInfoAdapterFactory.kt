package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.productdetail.uidata.*

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoAdapterFactory {
    fun type(data: ProductDetailInfoHeaderDataModel): Int
    fun type(data: ProductDetailInfoLoadingDataModel): Int
    fun type(data: ProductDetailInfoExpandableDataModel): Int
    fun type(data: ProductDetailInfoExpandableImageDataModel): Int
    fun type(data: ProductDetailInfoExpandableListDataModel): Int
    fun type(data: ProductDetailInfoDiscussionDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}