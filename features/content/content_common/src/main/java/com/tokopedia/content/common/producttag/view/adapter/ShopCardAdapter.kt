package com.tokopedia.content.common.producttag.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.producttag.view.adapter.delegate.ShopCardAdapterDelegate
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
class ShopCardAdapter(
    onSelected: (ShopUiModel, Int) -> Unit,
    private val onLoading: () -> Unit,
) : BaseDiffUtilAdapter<ShopCardAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(ShopCardAdapterDelegate.Shop(onSelected))
            .addDelegate(ShopCardAdapterDelegate.Loading())
            .addDelegate(ShopCardAdapterDelegate.EmptyState())
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
        return if(oldItem is Model.Shop && newItem is Model.Shop) {
            oldItem.shop.shopId == newItem.shop.shopId
        } else if(oldItem is Model.EmptyState && newItem is Model.EmptyState) {
            oldItem.hasFilterApplied == newItem.hasFilterApplied
        } else if(oldItem is Model.Loading && newItem is Model.Loading) false
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
        data class Shop(
            val shop: ShopUiModel,
        ) : Model

        object Loading: Model

        data class EmptyState(
            val hasFilterApplied: Boolean,
        ) : Model
    }
}