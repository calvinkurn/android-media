package com.tokopedia.product.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel

interface DynamicProductDetailAdapterFactory {
    fun type(data: ProductSnapshotDataModel) : Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}