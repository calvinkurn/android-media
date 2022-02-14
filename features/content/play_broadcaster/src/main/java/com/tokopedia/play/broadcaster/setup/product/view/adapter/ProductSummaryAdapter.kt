package com.tokopedia.play.broadcaster.setup.product.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductSummaryViewHolder
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
internal class ProductSummaryAdapter(
    listener: ProductSummaryViewHolder.Body.Listener
) : BaseDiffUtilAdapter<ProductSummaryAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(ProductSummaryAdapterDelegate.Placeholder())
            .addDelegate(ProductSummaryAdapterDelegate.Header())
            .addDelegate(ProductSummaryAdapterDelegate.Body(listener))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return if(oldItem is Model.Body && newItem is Model.Body) {
            oldItem.product.id == newItem.product.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed class Model {
        object Placeholder: Model()
        data class Header(val text: String, val status: CampaignStatus): Model()
        data class Body(val product: ProductUiModel): Model()
    }
}