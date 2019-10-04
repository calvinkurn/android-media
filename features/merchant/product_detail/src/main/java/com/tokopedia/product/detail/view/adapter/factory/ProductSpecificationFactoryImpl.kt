package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.product.detail.data.model.spesification.SpecificationBodyDataModel
import com.tokopedia.product.detail.data.model.spesification.SpecificationTitleDataModel
import com.tokopedia.product.detail.view.viewholder.ProductSpecificationBodyViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductSpecificationTitleViewHolder

class ProductSpecificationFactoryImpl : BaseAdapterTypeFactory(), ProductSpecificationFactory {

    override fun type(dataModel: SpecificationTitleDataModel): Int {
        return SpecificationTitleDataModel.LAYOUT
    }

    override fun type(dataModel: SpecificationBodyDataModel): Int {
        return SpecificationBodyDataModel.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            LoadingShimmeringGridViewHolder.LAYOUT -> LoadingShimmeringGridViewHolder(view)
            SpecificationBodyDataModel.LAYOUT -> ProductSpecificationBodyViewHolder(view)
            SpecificationTitleDataModel.LAYOUT -> ProductSpecificationTitleViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

}