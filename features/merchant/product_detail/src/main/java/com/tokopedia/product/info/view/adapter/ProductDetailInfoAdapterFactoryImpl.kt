package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoExpandableViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoHeaderViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoLoadingViewHolder

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoAdapterFactoryImpl(private val listener: ProductDetailInfoListener) : BaseAdapterTypeFactory(), ProductDetailInfoAdapterFactory {

    override fun type(data: ProductDetailInfoHeaderDataModel): Int {
        return ProductDetailInfoHeaderViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoLoadingDataModel): Int {
        return ProductDetailInfoLoadingViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoExpandableDataModel): Int {
        return ProductDetailInfoExpandableViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductDetailInfoHeaderViewHolder.LAYOUT -> ProductDetailInfoHeaderViewHolder(view)
            ProductDetailInfoLoadingViewHolder.LAYOUT -> ProductDetailInfoLoadingViewHolder(view, listener)
            ProductDetailInfoExpandableViewHolder.LAYOUT -> ProductDetailInfoExpandableViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }
}