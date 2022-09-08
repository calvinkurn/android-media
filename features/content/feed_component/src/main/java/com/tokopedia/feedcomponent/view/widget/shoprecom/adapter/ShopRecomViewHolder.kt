package com.tokopedia.feedcomponent.view.widget.shoprecom.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.view.widget.shoprecom.ShopRecomView
import com.tokopedia.feedcomponent.view.widget.shoprecom.listener.ShopRecommendationCallback

/**
 * created by fachrizalmrsln on 13/07/22
 **/
class ShopRecomViewHolder(
    private val view: ShopRecomView,
    private val listener: ShopRecommendationCallback
) : RecyclerView.ViewHolder(view) {

    fun bindData(item: ShopRecomUiModelItem) {
        view.setData(item, adapterPosition)
        view.setListener(listener)
    }

}