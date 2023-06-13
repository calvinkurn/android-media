package com.tokopedia.feedcomponent.shoprecom.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.view.ShopRecomView

/**
 * created by fachrizalmrsln on 13/07/22
 **/
class ShopRecomViewHolder(
    private val view: ShopRecomView,
    private val listener: ShopRecomWidgetCallback
) : RecyclerView.ViewHolder(view) {

    fun bindData(item: ShopRecomUiModelItem) {
        view.setData(item, bindingAdapterPosition)
        view.setListener(listener)
    }

    companion object {
        fun create(parent: ViewGroup, listener: ShopRecomWidgetCallback) = ShopRecomViewHolder(
            ShopRecomView(parent.context), listener
        )
    }
}
