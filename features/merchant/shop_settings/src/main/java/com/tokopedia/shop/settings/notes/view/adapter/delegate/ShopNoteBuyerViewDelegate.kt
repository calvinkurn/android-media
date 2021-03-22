package com.tokopedia.shop.settings.notes.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.shop.settings.notes.data.ShopNoteBuyerViewUiModel
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteBuyerViewViewHolder

class ShopNoteBuyerViewDelegate: TypedAdapterDelegate<ShopNoteBuyerViewUiModel, ShopNoteBuyerViewUiModel, ShopNoteBuyerViewViewHolder>(ShopNoteBuyerViewViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ShopNoteBuyerViewUiModel, holder: ShopNoteBuyerViewViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ShopNoteBuyerViewViewHolder {
        return  ShopNoteBuyerViewViewHolder(basicView)
    }

}