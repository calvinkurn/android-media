package com.tokopedia.feedcomponent.shoprecom.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 13/07/22
 **/
class ShopRecomAdapter(
    private val listener: ShopRecomWidgetCallback
) : BaseDiffUtilAdapter<ShopRecomAdapter.Model>(), ShopRecomWidgetCallback {

    init {
        delegatesManager
            .addDelegate(ShopRecomAdapterDelegate.Loading())
            .addDelegate(ShopRecomAdapterDelegate.ShopRecomWidget(listener))
    }

    override fun onShopRecomCloseClicked(itemID: Long) {
        listener.onShopRecomCloseClicked(itemID)
    }

    override fun onShopRecomFollowClicked(itemID: Long) {
        listener.onShopRecomFollowClicked(itemID)
    }

    override fun onShopRecomItemClicked(
        itemID: Long,
        appLink: String,
        imageUrl: String,
        postPosition: Int
    ) {
        listener.onShopRecomItemClicked(itemID, appLink, imageUrl, postPosition)
    }

    override fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int) {
        listener.onShopRecomItemImpress(item, postPosition)
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
