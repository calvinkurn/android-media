package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.productdetail.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.model.productdetail.ProductDetailInfoLoadingDataModel

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoAdapterFactory {
    fun type(data: ProductDetailInfoHeaderDataModel): Int
    fun type(data: ProductDetailInfoLoadingDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}