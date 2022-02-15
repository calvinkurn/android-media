package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductSectionAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.ProductSectionViewHolder
import com.tokopedia.play.view.uimodel.PlayProductSectionUiModel

/**
 * @author by astidhiyaa on 02/02/22
 */
class ProductSectionAdapter(
    listener: ProductSectionViewHolder.Listener
) : BaseDiffUtilAdapter<PlayProductSectionUiModel>() {

    init {
        //TODO() = shimmering
        delegatesManager.addDelegate(ProductSectionAdapterDelegate(listener))
    }

    override fun areItemsTheSame(
        oldItem: PlayProductSectionUiModel,
        newItem: PlayProductSectionUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayProductSectionUiModel,
        newItem: PlayProductSectionUiModel
    ): Boolean {
        return oldItem == newItem
    }
}