package com.tokopedia.feedcomponent.shoprecom.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 13/07/22
 **/
class ShopRecomAdapter(
    listener: ShopRecomWidgetCallback,
    private val onLoading: () -> Unit,
) : BaseDiffUtilAdapter<ShopRecomAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(ShopRecomAdapterDelegate.Loading())
            .addDelegate(ShopRecomAdapterDelegate.ShopRecomWidget(listener))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (position == (itemCount - 1) && holder is ShopRecomLoadingViewHolder) onLoading()
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Loading && newItem is Model.Loading -> false
            oldItem is Model.ShopRecomWidget && newItem is Model.ShopRecomWidget -> oldItem.shopRecomItem.id == newItem.shopRecomItem.id
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
        object Loading : Model
        data class ShopRecomWidget(val shopRecomItem: ShopRecomUiModelItem) : Model
    }

}
