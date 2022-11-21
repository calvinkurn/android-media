package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.view.models.ProductDetailInfoAnnotationDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoCardDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoCatalogDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoLoadingDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoLoadingDescriptionDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoLoadingSpecificationDataModel

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
    fun type(data: ProductDetailInfoCatalogDataModel): Int
    fun type(data: ProductDetailInfoLoadingSpecificationDataModel): Int
    fun type(data: ProductDetailInfoLoadingDescriptionDataModel): Int
    fun type(data: ProductDetailInfoAnnotationDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
