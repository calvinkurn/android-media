package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.spesification.SpecificationBodyDataModel
import com.tokopedia.product.detail.data.model.spesification.SpecificationTitleDataModel

interface ProductSpecificationFactory {
    fun type(dataModel: SpecificationTitleDataModel): Int
    fun type(dataModel: SpecificationBodyDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}