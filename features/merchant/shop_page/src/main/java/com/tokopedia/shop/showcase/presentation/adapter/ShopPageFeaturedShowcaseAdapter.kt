package com.tokopedia.shop.showcase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.showcase.presentation.adapter.viewholder.ShopPageFeaturedShowcaseListener
import com.tokopedia.shop.showcase.presentation.adapter.viewholder.ShopPageFeaturedShowcaseViewHolder
import com.tokopedia.shop.showcase.presentation.model.FeaturedShowcaseUiModel

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseAdapter(
        private val shopPageFeaturedShowcaseListener: ShopPageFeaturedShowcaseListener
) : RecyclerView.Adapter<ShopPageFeaturedShowcaseViewHolder>() {

    private var showcaseFeaturedList: List<FeaturedShowcaseUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPageFeaturedShowcaseViewHolder {
        return ShopPageFeaturedShowcaseViewHolder(
                itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_shop_featured_showcase,
                        parent,
                        false
                ),
                listener = shopPageFeaturedShowcaseListener
        )
    }

    override fun getItemCount(): Int {
        return showcaseFeaturedList.size
    }

    override fun onBindViewHolder(holder: ShopPageFeaturedShowcaseViewHolder, position: Int) {
        holder.bind(showcaseFeaturedList)
    }

    fun updateFeaturedShowcaseDataset(newList: List<FeaturedShowcaseUiModel>) {
        showcaseFeaturedList = newList
        notifyDataSetChanged()
    }

}