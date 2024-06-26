package com.tokopedia.content.product.picker.seller.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.view.viewholder.ProductSummaryViewHolder

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
class ProductSummaryAdapter(
    listener: ProductSummaryViewHolder.Body.Listener
) : BaseDiffUtilAdapter<ProductSummaryAdapter.Model>() {

    init {
        delegatesManager
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
        data class Header(val text: String, val status: CampaignStatus): Model()
        data class Body(
            val product: ProductUiModel,
            val isEligibleForPin: Boolean,
            val isNumerationShown: Boolean,
            val selectedAccount: ContentAccountUiModel,
        ) : Model()
    }
}
