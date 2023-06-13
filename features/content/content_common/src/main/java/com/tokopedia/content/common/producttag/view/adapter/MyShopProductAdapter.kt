package com.tokopedia.content.common.producttag.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.producttag.view.adapter.delegate.MyShopProductAdapterDelegate
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
class MyShopProductAdapter(
    onSelected: (ProductUiModel, Int) -> Unit,
    private val onLoading: () -> Unit,
) : BaseDiffUtilAdapter<MyShopProductAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(MyShopProductAdapterDelegate.Product(onSelected))
            .addDelegate(MyShopProductAdapterDelegate.ProductWithCheckbox(onSelected))
            .addDelegate(MyShopProductAdapterDelegate.Loading())
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

        data class ProductWithCheckbox(
            val product: ProductUiModel,
            val isSelected: Boolean,
        ) : Model

        object Loading: Model
    }
}