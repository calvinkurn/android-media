package com.tokopedia.product.detail.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel

/**
 * Created by Yehezkiel on 04/01/21
 */
class ProductDetailDiffUtil : DiffUtil.ItemCallback<DynamicPdpDataModel>() {

    override fun areItemsTheSame(oldItem: DynamicPdpDataModel, newItem: DynamicPdpDataModel): Boolean {
        return oldItem.name() == newItem.name()
    }

    override fun areContentsTheSame(oldItem: DynamicPdpDataModel, newItem: DynamicPdpDataModel): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(oldItem: DynamicPdpDataModel, newItem: DynamicPdpDataModel): Any? {
        return oldItem.getChangePayload(newItem)
    }
}