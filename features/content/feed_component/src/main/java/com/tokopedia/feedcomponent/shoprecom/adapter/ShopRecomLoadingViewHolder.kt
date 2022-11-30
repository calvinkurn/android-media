package com.tokopedia.feedcomponent.shoprecom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.databinding.ItemShopRecommendationLoadingBinding

/**
 * created by fachrizalmrsln on 03/11/22
 **/
class ShopRecomLoadingViewHolder(
    binding: ItemShopRecommendationLoadingBinding,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup) = ShopRecomLoadingViewHolder(
            ItemShopRecommendationLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }
}
