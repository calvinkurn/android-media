package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductChangeGridSectionUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel

class ShopPageHomeDiffUtilCallback(
        private val oldItems: List<Visitable<*>>,
        private val newItems: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        if (isItemMatchWithUiModel<ShopHomeProductChangeGridSectionUiModel>(oldItem, newItem)) {
            return false
        }
        if (isItemMatchWithUiModel<ShopProductSortFilterUiModel>(oldItem, newItem)) {
            return false
        }
        return oldItem == newItem
    }

    private inline fun <reified T> isItemMatchWithUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is T && newItem is T
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        if(isItemMatchWithUiModel<BaseShopHomeWidgetUiModel>(oldItem, newItem)){
            val oldShopHomeWidgetData = oldItem as? BaseShopHomeWidgetUiModel
            val newShopHomeWidgetData = newItem as? BaseShopHomeWidgetUiModel
            return oldShopHomeWidgetData?.isNewData == false && newShopHomeWidgetData?.isNewData == false
        }
        if(isItemMatchWithUiModel<ThematicWidgetUiModel>(oldItem, newItem)){
            val oldShopHomeWidgetData = oldItem as? ThematicWidgetUiModel
            val newShopHomeWidgetData = newItem as? ThematicWidgetUiModel
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