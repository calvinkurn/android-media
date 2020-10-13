package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDataModel

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoAdapterFactory {
    fun type(data: ProductDetailInfoHeaderDataModel): Int
    fun type(data: ProductDetailInfoLoadingDataModel): Int
    fun type(data: ProductDetailInfoExpandableDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}