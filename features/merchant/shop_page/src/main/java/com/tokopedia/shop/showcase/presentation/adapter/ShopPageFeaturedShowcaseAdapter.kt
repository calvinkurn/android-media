package com.tokopedia.shop.showcase.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.showcase.presentation.adapter.viewholder.ShopPageFeaturedShowcaseViewHolder

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseAdapter : RecyclerView.Adapter<ShopPageFeaturedShowcaseViewHolder>() {

    private var showcaseFeaturedList: List<ShopEtalaseModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPageFeaturedShowcaseViewHolder {
        return ShopPageFeaturedShowcaseViewHolder(
                parent.inflateLayout(R.layout.item_shop_featured_showcase)
        )
    }

    override fun getItemCount(): Int {
        return showcaseFeaturedList.size
    }

    override fun onBindViewHolder(holder: ShopPageFeaturedShowcaseViewHolder, position: Int) {
        holder.bind(showcaseFeaturedList[position])
    }

    fun updateFeaturedShowcaseDataset(newList: List<ShopEtalaseModel>) {
        showcaseFeaturedList = newList
        notifyDataSetChanged()
    }

}