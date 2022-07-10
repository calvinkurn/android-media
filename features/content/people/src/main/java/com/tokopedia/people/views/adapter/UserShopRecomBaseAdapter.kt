package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.feedcomponent.data.pojo.people.ShopRecomItemUI
import com.tokopedia.feedcomponent.view.widget.shoprecom.ShopRecomView
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.people.databinding.UpLayoutShopRecommendationItemBinding
import com.tokopedia.people.model.ShopRecomItem
import com.tokopedia.people.model.UserShopRecomModel

open class UserShopRecomBaseAdapter(
    callback: AdapterCallback,
    private val shopRecomCallback: ShopRecommendationCallback
) : BaseAdapter<ShopRecomItem>(callback), ShopRecomView.Listener {

    interface ShopRecommendationCallback {
        fun onCloseClicked(data: ShopRecomItem)
        fun onFollowClicked(encryptedID: String)
        fun onItemClicked(shopID: Long)
    }

    var cursor: String = ""

    inner class ViewHolder(view: UpLayoutShopRecommendationItemBinding) : BaseVH(view.root) {
        internal var shopRecomView: ShopRecomView = view.peopleShopRecommendation
        override fun bindView(item: ShopRecomItem, position: Int) {
            setData(this, item)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(UpLayoutShopRecommendationItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun loadData(currentPageIndex: Int, vararg args: String?) {
        super.loadData(currentPageIndex, *args)
    }

    fun onSuccess(data: UserShopRecomModel) {
        if (data.feedXRecomWidget.items.isEmpty()) {
            loadCompleted(listOf(), data)
            isLastPage = true
            cursor = ""
        } else {
            data.feedXRecomWidget.items.let { loadCompleted(it, data) }
            isLastPage = true
            cursor = ""
        }
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, shopRecomItem: ShopRecomItem) {
        holder.shopRecomView.setData(
            ShopRecomItemUI(
                badgeImageURL = shopRecomItem.badgeImageURL,
                encryptedID = shopRecomItem.encryptedID,
                logoImageURL = shopRecomItem.logoImageURL,
                id = shopRecomItem.id,
                name = shopRecomItem.name,
                nickname = shopRecomItem.nickname,
                type = shopRecomItem.type
            )
        )
        holder.shopRecomView.setListener(this)
    }

    override fun onCloseClicked(data: ShopRecomItemUI) {
        shopRecomCallback.onCloseClicked(
            ShopRecomItem(
                badgeImageURL = data.badgeImageURL,
                encryptedID = data.encryptedID,
                logoImageURL = data.logoImageURL,
                id = data.id,
                name = data.name,
                nickname = data.nickname,
                type = data.type
            )
        )
    }

    override fun onFollowClicked(encryptedID: String) {
        shopRecomCallback.onFollowClicked(encryptedID)
    }

    override fun onShopItemClicked(shopID: Long) {
        shopRecomCallback.onItemClicked(shopID)
    }

}
