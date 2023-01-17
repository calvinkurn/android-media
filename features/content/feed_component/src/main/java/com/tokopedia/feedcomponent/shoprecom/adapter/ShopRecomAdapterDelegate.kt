package com.tokopedia.feedcomponent.shoprecom.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
internal class ShopRecomAdapterDelegate private constructor() {

    internal class ShopRecomWidget(private val listener: ShopRecomWidgetCallback) :
        TypedAdapterDelegate<
            ShopRecomAdapter.Model.ShopRecomWidget,
            ShopRecomAdapter.Model,
            ShopRecomViewHolder>(R.layout.item_shop_recommendation) {

        override fun onBindViewHolder(
            item: ShopRecomAdapter.Model.ShopRecomWidget,
            holder: ShopRecomViewHolder
        ) {
            holder.bindData(item.shopRecomItem)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ShopRecomViewHolder {
            return ShopRecomViewHolder.create(parent, listener)
        }
    }

    internal class Loading : TypedAdapterDelegate<
        ShopRecomAdapter.Model.Loading,
        ShopRecomAdapter.Model,
        ShopRecomLoadingViewHolder>(R.layout.item_shop_recommendation_loading) {

        override fun onBindViewHolder(
            item: ShopRecomAdapter.Model.Loading,
            holder: ShopRecomLoadingViewHolder
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ShopRecomLoadingViewHolder {
            return ShopRecomLoadingViewHolder.create(parent)
        }
    }

}
