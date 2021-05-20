package com.tokopedia.shop.note.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.shop.note.data.ShopNoteBottomSheetUiModel
import com.tokopedia.shop.note.view.adapter.delegate.ShopNoteBottomSheetDelegate

class ShopNoteBottomSheetAdapter: BaseDiffUtilAdapter<ShopNoteBottomSheetUiModel>() {

    init {
        delegatesManager.addDelegate(ShopNoteBottomSheetDelegate())
    }

    override fun areItemsTheSame(oldItem: ShopNoteBottomSheetUiModel, newItem: ShopNoteBottomSheetUiModel): Boolean =
            oldItem.title == newItem.title && oldItem.description == newItem.description

    override fun areContentsTheSame(oldItem: ShopNoteBottomSheetUiModel, newItem: ShopNoteBottomSheetUiModel): Boolean =
            oldItem == newItem
}