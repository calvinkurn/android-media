package com.tokopedia.feedcomponent.view.widget.shoprecom.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.view.widget.shoprecom.ShopRecomView
import com.tokopedia.feedcomponent.view.widget.shoprecom.adapter.ShopRecomViewHolder
import com.tokopedia.feedcomponent.view.widget.shoprecom.listener.ShopRecommendationCallback
import androidx.recyclerview.widget.DiffUtil




/**
 * created by fachrizalmrsln on 13/07/22
 **/
class ShopRecomAdapter(
    private val shopRecomCallback: ShopRecommendationCallback
) : RecyclerView.Adapter<ShopRecomViewHolder>(), ShopRecommendationCallback {

    private val shopRecomItem = mutableListOf<ShopRecomUiModelItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopRecomViewHolder {
        return ShopRecomViewHolder(ShopRecomView(parent.context), shopRecomCallback)
    }

    override fun onBindViewHolder(holder: ShopRecomViewHolder, position: Int) {
        holder.bindData(shopRecomItem[position])
    }

    override fun getItemCount(): Int {
        return shopRecomItem.size
    }

    var cursor: String = ""

    fun insertItem(data: List<ShopRecomUiModelItem>) {
        val diffCallback = ShopRecomDiffUtil(shopRecomItem, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        shopRecomItem.addAll(data)
        cursor = ""
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(item: ShopRecomUiModelItem) {
        val position = shopRecomItem.indexOf(item.copy(isFollow = !item.isFollow))
        shopRecomItem[position].isFollow = item.isFollow
        notifyItemChanged(position, item)
    }

    fun removeItem(item: ShopRecomUiModelItem) {
        val position: Int = shopRecomItem.indexOf(item)
        if (position > -1) {
            shopRecomItem.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onShopRecomCloseClicked(item: ShopRecomUiModelItem) {
        shopRecomCallback.onShopRecomCloseClicked(item)
    }

    override fun onShopRecomFollowClicked(item: ShopRecomUiModelItem) {
        shopRecomCallback.onShopRecomFollowClicked(item)
    }

    override fun onShopRecomItemClicked(appLink: String) {
        shopRecomCallback.onShopRecomItemClicked(appLink)
    }

}
