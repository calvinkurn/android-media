package com.tokopedia.createpost.producttag.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.createpost.producttag.view.adapter.delegate.ProductTagCardAdapterDelegate
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagCardAdapter(
    onSelected: (ProductUiModel) -> Unit,
    private val onLoading: () -> Unit,
) : BaseDiffUtilAdapter<ProductTagCardAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(ProductTagCardAdapterDelegate.Suggestion())
            .addDelegate(ProductTagCardAdapterDelegate.Ticker())
            .addDelegate(ProductTagCardAdapterDelegate.Product(onSelected))
            .addDelegate(ProductTagCardAdapterDelegate.Loading())
            .addDelegate(ProductTagCardAdapterDelegate.EmptyState())
            .addDelegate(ProductTagCardAdapterDelegate.RecommendationTitle())
            .addDelegate(ProductTagCardAdapterDelegate.Divider())
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
        } else if(oldItem is Model.Loading && newItem is Model.Loading) false
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {

        data class Suggestion(
            val text: String,
        ) : Model

        data class Ticker(
            val text: String,
            val onTickerClicked: () -> Unit,
            val onTickerClosed: () -> Unit,
        ) : Model

        data class Product(
            val product: ProductUiModel,
        ) : Model

        object Loading : Model

        data class EmptyState(
            val hasFilterApplied: Boolean,
            val onClicked: () -> Unit,
        ) : Model

        data class RecommendationTitle(
            val text: String,
        ) : Model

        object Divider : Model
    }
}