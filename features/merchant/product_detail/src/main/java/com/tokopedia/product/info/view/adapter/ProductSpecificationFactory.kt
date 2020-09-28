package com.tokopedia.product.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.info.model.specification.SpecificationBodyDataModel
import com.tokopedia.product.info.model.specification.SpecificationTitleDataModel

interface ProductSpecificationFactory {
    fun type(dataModel: SpecificationTitleDataModel): Int
    fun type(dataModel: SpecificationBodyDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}