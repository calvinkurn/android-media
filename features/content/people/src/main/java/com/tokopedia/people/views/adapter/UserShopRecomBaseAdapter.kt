package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.feedcomponent.data.pojo.people.ShopRecomItemUI
import com.tokopedia.feedcomponent.view.widget.shoprecom.ShopRecomView
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.people.databinding.UpLayoutShopRecommendationItemBinding
import com.tokopedia.people.views.uimodel.shoprecom.ShopRecomUiModelItem

open class UserShopRecomBaseAdapter(
    callback: AdapterCallback,
    private val shopRecomCallback: ShopRecommendationCallback
) : BaseAdapter<ShopRecomUiModelItem>(callback), ShopRecomView.Listener {

    interface ShopRecommendationCallback {
        fun onShopRecomCloseClicked(item: ShopRecomUiModelItem)
        fun onShopRecomFollowClicked(item: ShopRecomUiModelItem)
        fun onShopRecomItemClicked(appLink: String)
    }

    var cursor: String = ""

    inner class ViewHolder(view: UpLayoutShopRecommendationItemBinding) : BaseVH(view.root) {
        internal var shopRecomView: ShopRecomView = view.peopleShopRecommendation
        override fun bindView(item: ShopRecomUiModelItem, position: Int) {
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

    fun onSuccess(data: List<ShopRecomUiModelItem>) {
        if (data.isEmpty()) {
            loadCompleted(emptyList(), data)
            isLastPage = true
            cursor = ""
        } else {
            loadCompleted(data, data)
            isLastPage = true
            cursor = ""
        }
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: ShopRecomUiModelItem) {
        holder.shopRecomView.setData(item.transformToShopRecomItemUI())
        holder.shopRecomView.setListener(this)
    }

    fun updateItem(item: ShopRecomUiModelItem) {
        val position = items.indexOf(item.copy(isFollow = !item.isFollow))
        items[position].isFollow = item.isFollow
        notifyItemChanged(position, item)
    }

    override fun onShopRecomCloseClicked(item: ShopRecomItemUI) {
        shopRecomCallback.onShopRecomCloseClicked(item.transformToShopRecomItem())
    }

    override fun onShopRecomFollowClicked(item: ShopRecomItemUI) {
        shopRecomCallback.onShopRecomFollowClicked(item.transformToShopRecomItem())
    }

    override fun onShopRecomItemClicked(appLink: String) {
        shopRecomCallback.onShopRecomItemClicked(appLink)
    }

    private fun ShopRecomUiModelItem.transformToShopRecomItemUI(): ShopRecomItemUI {
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

    private fun ShopRecomItemUI.transformToShopRecomItem(): ShopRecomUiModelItem {
        return ShopRecomUiModelItem(
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
