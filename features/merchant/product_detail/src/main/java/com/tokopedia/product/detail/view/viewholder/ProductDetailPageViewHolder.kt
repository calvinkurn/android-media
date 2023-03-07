package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel

abstract class ProductDetailPageViewHolder<T : DynamicPdpDataModel>(
    itemView: View
) : AbstractViewHolder<T>(itemView) {

    protected fun getComponentTrackData(element: T) = ComponentTrackDataModel(
        element.type(),
        element.name(),
        adapterPosition + 1
    )
}
