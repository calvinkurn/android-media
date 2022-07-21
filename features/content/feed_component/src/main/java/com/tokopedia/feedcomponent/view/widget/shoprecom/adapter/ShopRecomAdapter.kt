package com.tokopedia.feedcomponent.view.widget.shoprecom.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.view.widget.shoprecom.ShopRecomView
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
        shopRecomCallback.onShopRecomItemImpress(shopRecomItem[position], position)
        holder.bindData(shopRecomItem[position])
    }

    override fun getItemCount(): Int {
        return shopRecomItem.size
    }

    fun updateData(data: List<ShopRecomUiModelItem>) {
        val diffCallback = ShopRecomDiffUtil(shopRecomItem, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        shopRecomItem.clear()
        shopRecomItem.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onShopRecomCloseClicked(itemID: Long) {
        shopRecomCallback.onShopRecomCloseClicked(itemID)
    }

    override fun onShopRecomFollowClicked(itemID: Long) {
        shopRecomCallback.onShopRecomFollowClicked(itemID)
    }

    override fun onShopRecomItemClicked(itemID: Long, appLink: String, imageUrl: String, postPosition: Int) {
        shopRecomCallback.onShopRecomItemClicked(itemID, appLink, imageUrl, postPosition)
    }

    override fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int) {
        shopRecomCallback.onShopRecomItemImpress(item, postPosition)
    }

}
