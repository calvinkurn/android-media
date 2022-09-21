package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.view.ProductDetailInfoListener
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
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoAnnotationViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoCardViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoCatalogViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoDiscussionViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoExpandableImageViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoExpandableListViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoExpandableViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoHeaderViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoLoadingDescriptionViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoLoadingSpecificationViewHolder
import com.tokopedia.product.info.view.viewholder.productdetail.ProductDetailInfoLoadingViewHolder

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoAdapterFactoryImpl(
    private val listener: ProductDetailInfoListener
) : BaseAdapterTypeFactory(), ProductDetailInfoAdapterFactory {

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

    override fun type(data: ProductDetailInfoCardDataModel): Int {
        return ProductDetailInfoCardViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoCatalogDataModel): Int {
        return ProductDetailInfoCatalogViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoLoadingSpecificationDataModel): Int {
        return ProductDetailInfoLoadingSpecificationViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoLoadingDescriptionDataModel): Int {
        return ProductDetailInfoLoadingDescriptionViewHolder.LAYOUT
    }

    override fun type(data: ProductDetailInfoAnnotationDataModel): Int {
        return ProductDetailInfoAnnotationViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> = when (type) {
        ProductDetailInfoHeaderViewHolder.LAYOUT -> ProductDetailInfoHeaderViewHolder(
            view = view,
        )
        ProductDetailInfoLoadingViewHolder.LAYOUT -> ProductDetailInfoLoadingViewHolder(view)
        ProductDetailInfoExpandableViewHolder.LAYOUT -> ProductDetailInfoExpandableViewHolder(
            view = view,
            listener = listener
        )
        ProductDetailInfoExpandableImageViewHolder.LAYOUT -> ProductDetailInfoExpandableImageViewHolder(
            view = view,
            listener = listener
        )
        ProductDetailInfoExpandableListViewHolder.LAYOUT -> ProductDetailInfoExpandableListViewHolder(
            view = view,
            listener = listener
        )
        ProductDetailInfoDiscussionViewHolder.LAYOUT -> ProductDetailInfoDiscussionViewHolder(
            view = view,
            listener = listener
        )
        ProductDetailInfoCardViewHolder.LAYOUT -> ProductDetailInfoCardViewHolder(
            view = view,
            listener = listener
        )
        ProductDetailInfoCatalogViewHolder.LAYOUT -> ProductDetailInfoCatalogViewHolder(
            view = view,
            listener = listener
        )
        ProductDetailInfoLoadingSpecificationViewHolder.LAYOUT -> ProductDetailInfoLoadingSpecificationViewHolder(
            view = view
        )
        ProductDetailInfoLoadingDescriptionViewHolder.LAYOUT -> ProductDetailInfoLoadingDescriptionViewHolder(
            view = view
        )
        ProductDetailInfoAnnotationViewHolder.LAYOUT -> ProductDetailInfoAnnotationViewHolder(
            view = view,
            listener = listener
        )
        else -> super.createViewHolder(view, type)
    }
}
