package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductLineAdapterDelegate
import com.tokopedia.play.view.type.ProductLineUiModel

/**
 * Created by jegul on 03/03/20
 */
class ProductLineAdapter : BaseDiffUtilAdapter<ProductLineUiModel>() {

    init {
        delegatesManager
                .addDelegate(ProductLineAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductLineUiModel, newItem: ProductLineUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductLineUiModel, newItem: ProductLineUiModel): Boolean {
        return oldItem == newItem
    }
}