package com.tokopedia.shop.campaign.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

class ShopPageCampaignDiffUtilCallback(
    private val oldItems: List<Visitable<*>>,
    private val newItems: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private inline fun <reified T> isItemMatchWithUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is T && newItem is T
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        if (isItemMatchWithUiModel<BaseShopHomeWidgetUiModel>(oldItem, newItem)) {
            val oldShopHomeWidgetData = oldItem as? BaseShopHomeWidgetUiModel
            val newShopHomeWidgetData = newItem as? BaseShopHomeWidgetUiModel
            return oldShopHomeWidgetData?.isNewData == false && newShopHomeWidgetData?.isNewData == false
        }
        return oldItem == newItem
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }
}
