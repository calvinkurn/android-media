package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.productdetail.uidata.*
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.viewholder.productdetail.*

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

    override fun type(data: ProductDetailInfoExpandableImageDataModel): Int {
        return ProductDetailInfoExpandableImageViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoExpandableListDataModel): Int {
        return ProductDetailInfoExpandableListViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoDiscussionDataModel): Int {
        return ProductDetailInfoDiscussionViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductDetailInfoHeaderViewHolder.LAYOUT -> ProductDetailInfoHeaderViewHolder(view, listener)
            ProductDetailInfoLoadingViewHolder.LAYOUT -> ProductDetailInfoLoadingViewHolder(view)
            ProductDetailInfoExpandableViewHolder.LAYOUT -> ProductDetailInfoExpandableViewHolder(view, listener)
            ProductDetailInfoExpandableImageViewHolder.LAYOUT -> ProductDetailInfoExpandableImageViewHolder(view, listener)
            ProductDetailInfoExpandableListViewHolder.LAYOUT -> ProductDetailInfoExpandableListViewHolder(view, listener)
            ProductDetailInfoDiscussionViewHolder.LAYOUT -> ProductDetailInfoDiscussionViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }
}