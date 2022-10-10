package com.tokopedia.feedcomponent.shoprecom.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.view.ShopRecomView
import com.tokopedia.feedcomponent.shoprecom.listener.ShopRecomCallback

/**
 * created by fachrizalmrsln on 13/07/22
 **/
class ShopRecomViewHolder(
    private val view: ShopRecomView,
    private val listener: ShopRecomCallback
) : RecyclerView.ViewHolder(view) {

    fun bindData(item: ShopRecomUiModelItem) {
        view.setData(item, bindingAdapterPosition)
        view.setListener(listener)
    }

}
