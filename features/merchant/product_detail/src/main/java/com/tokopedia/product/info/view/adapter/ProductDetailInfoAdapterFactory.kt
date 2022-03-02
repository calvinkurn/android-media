package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoCardDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDataModel

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
    fun type(data: ProductDetailInfoCardDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}