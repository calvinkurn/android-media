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
        fun onCloseClicked(item: ShopRecomItem)
        fun onFollowClicked(item: ShopRecomItem)
        fun onItemClicked(appLink: String)
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

    private fun setData(holder: ViewHolder, item: ShopRecomItem) {
        holder.shopRecomView.setData(item.transformToShopRecomItemUI())
        holder.shopRecomView.setListener(this)
    }

    fun updateItem(item: ShopRecomItem) {
        val position = items.indexOf(item.copy(isFollow = !item.isFollow))
        items[position].isFollow = item.isFollow
        notifyItemChanged(position, item)
    }

    override fun onCloseClicked(item: ShopRecomItemUI) {
        shopRecomCallback.onCloseClicked(item.transformToShopRecomItem())
    }

    override fun onFollowClicked(item: ShopRecomItemUI) {
        shopRecomCallback.onFollowClicked(item.transformToShopRecomItem())
    }

    override fun onShopItemClicked(appLink: String) {
        shopRecomCallback.onItemClicked(appLink)
    }

    private fun ShopRecomItem.transformToShopRecomItemUI(): ShopRecomItemUI {
        return ShopRecomItemUI(
            badgeImageURL = badgeImageURL,
            encryptedID = encryptedID,
            logoImageURL = logoImageURL,
            id = id,
            name = name,
            nickname = nickname,
            type = type,
            applink = applink,
            isFollow = isFollow
        )
    }

    private fun ShopRecomItemUI.transformToShopRecomItem(): ShopRecomItem {
        return ShopRecomItem(
            badgeImageURL = badgeImageURL,
            encryptedID = encryptedID,
            logoImageURL = logoImageURL,
            id = id,
            name = name,
            nickname = nickname,
            type = type,
            applink = applink,
            isFollow = isFollow
        )
    }

}
