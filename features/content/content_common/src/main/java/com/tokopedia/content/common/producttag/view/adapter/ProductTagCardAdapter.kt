package com.tokopedia.content.common.producttag.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.producttag.view.adapter.delegate.ProductTagCardAdapterDelegate
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagCardAdapter(
    onSelected: (ProductUiModel, Int) -> Unit,
    private val onLoading: () -> Unit,
) : BaseDiffUtilAdapter<ProductTagCardAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(ProductTagCardAdapterDelegate.Suggestion())
            .addDelegate(ProductTagCardAdapterDelegate.Ticker())
            .addDelegate(ProductTagCardAdapterDelegate.Product(onSelected))
            .addDelegate(ProductTagCardAdapterDelegate.ProductWithCheckbox(onSelected))
            .addDelegate(ProductTagCardAdapterDelegate.Loading())
            .addDelegate(ProductTagCardAdapterDelegate.EmptyState())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if(position == (itemCount - 1)) onLoading()
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return if(oldItem is Model.Product && newItem is Model.Product) {
            oldItem.product.id == newItem.product.id
        } else if(oldItem is Model.ProductWithCheckbox && newItem is Model.ProductWithCheckbox) {
            oldItem.product.id == newItem.product.id
        } else if(oldItem is Model.EmptyState && newItem is Model.EmptyState) {
            oldItem.hasFilterApplied == newItem.hasFilterApplied
        } else if(oldItem is Model.Loading && newItem is Model.Loading) false
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {

        data class Suggestion(
            val text: String,
            val onSuggestionClicked: () -> Unit,
        ) : Model

        data class Ticker(
            val text: String,
            val onTickerClicked: () -> Unit,
            val onTickerClosed: () -> Unit,
        ) : Model

        data class Product(
            val product: ProductUiModel,
        ) : Model

        data class ProductWithCheckbox(
            val product: ProductUiModel,
            val isSelected: Boolean,
        ) : Model

        object Loading : Model

        data class EmptyState(
            val hasFilterApplied: Boolean,
        ) : Model
    }
}