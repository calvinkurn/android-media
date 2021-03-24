package com.tokopedia.shop.showcase.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageListener
import com.tokopedia.shop.showcase.domain.model.ShopFeaturedShowcase
import com.tokopedia.shop.showcase.presentation.adapter.viewholder.ShopPageFeaturedShowcaseViewHolder

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseAdapter(
        private val shopShowcaseListImageListener: ShopShowcaseListImageListener
) : RecyclerView.Adapter<ShopPageFeaturedShowcaseViewHolder>() {

    private var showcaseFeaturedList: List<ShopFeaturedShowcase> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPageFeaturedShowcaseViewHolder {
        return ShopPageFeaturedShowcaseViewHolder(
                itemView = parent.inflateLayout(R.layout.item_shop_featured_showcase),
                listener = shopShowcaseListImageListener
        )
    }

    override fun getItemCount(): Int {
        return showcaseFeaturedList.size
    }

    override fun onBindViewHolder(holder: ShopPageFeaturedShowcaseViewHolder, position: Int) {
        holder.bind(showcaseFeaturedList)
    }

    fun updateFeaturedShowcaseDataset(newList: List<ShopFeaturedShowcase>) {
        showcaseFeaturedList = newList
        notifyDataSetChanged()
    }

}