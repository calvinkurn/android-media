package com.tokopedia.createpost.producttag.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.createpost.producttag.view.adapter.delegate.LastPurchasedProductAdapterDelegate
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
class LastPurchasedProductAdapter(
    onSelected: (ProductUiModel) -> Unit,
) : BaseDiffUtilAdapter<LastPurchasedProductAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(LastPurchasedProductAdapterDelegate.Product(onSelected))
            .addDelegate(LastPurchasedProductAdapterDelegate.Loading())
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
        data class Product(
            val product: ProductUiModel,
        ) : Model

        object Loading: Model
    }
}