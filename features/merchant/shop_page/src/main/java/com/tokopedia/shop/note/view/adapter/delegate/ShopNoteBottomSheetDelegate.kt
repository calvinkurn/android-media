package com.tokopedia.shop.note.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.shop.note.data.ShopNoteBottomSheetUiModel
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteBottomSheetViewHolder

class ShopNoteBottomSheetDelegate: TypedAdapterDelegate<ShopNoteBottomSheetUiModel, ShopNoteBottomSheetUiModel, ShopNoteBottomSheetViewHolder>(ShopNoteBottomSheetViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ShopNoteBottomSheetUiModel, holder: ShopNoteBottomSheetViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ShopNoteBottomSheetViewHolder {
        return  ShopNoteBottomSheetViewHolder(basicView)
    }

}