package com.tokopedia.shop.settings.notes.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.shop.settings.notes.data.ShopNoteBuyerViewUiModel
import com.tokopedia.shop.settings.notes.view.adapter.delegate.ShopNoteBuyerViewDelegate

class ShopNoteBuyerViewAdapter: BaseDiffUtilAdapter<ShopNoteBuyerViewUiModel>() {

    init {
        delegatesManager.addDelegate(ShopNoteBuyerViewDelegate())
    }

    override fun areItemsTheSame(oldItem: ShopNoteBuyerViewUiModel, newItem: ShopNoteBuyerViewUiModel): Boolean =
            oldItem.title == newItem.title && oldItem.description == newItem.description

    override fun areContentsTheSame(oldItem: ShopNoteBuyerViewUiModel, newItem: ShopNoteBuyerViewUiModel): Boolean =
            oldItem == newItem
}