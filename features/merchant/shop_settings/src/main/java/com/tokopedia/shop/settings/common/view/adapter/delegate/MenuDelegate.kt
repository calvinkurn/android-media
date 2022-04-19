package com.tokopedia.shop.settings.common.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.shop.settings.common.view.adapter.viewholder.MenuViewHolder

class MenuDelegate(private val listener: MenuViewHolder.ItemMenuListener?): TypedAdapterDelegate<String, String, MenuViewHolder>(MenuViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: String, holder: MenuViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MenuViewHolder {
        return  MenuViewHolder(listener, basicView)
    }

}