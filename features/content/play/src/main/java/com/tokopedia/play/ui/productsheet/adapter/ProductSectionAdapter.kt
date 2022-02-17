package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductPlaceholderAdapterDelegate
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductSectionAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.ProductSectionViewHolder
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * @author by astidhiyaa on 02/02/22
 */
class ProductSectionAdapter(
    listener: ProductSectionViewHolder.Listener
) : BaseDiffUtilAdapter<ProductSectionUiModel>() {

    init {
        delegatesManager
            .addDelegate(ProductSectionAdapterDelegate(listener))
            .addDelegate(ProductPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: ProductSectionUiModel,
        newItem: ProductSectionUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ProductSectionUiModel,
        newItem: ProductSectionUiModel
    ): Boolean {
        return oldItem == newItem
    }
}