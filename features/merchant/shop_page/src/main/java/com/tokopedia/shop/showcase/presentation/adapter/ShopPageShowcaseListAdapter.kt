package com.tokopedia.shop.showcase.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageViewHolder

/**
 * Created by Rafli Syam on 12/03/2021
 */
class ShopPageShowcaseListAdapter : RecyclerView.Adapter<ShopShowcaseListImageViewHolder>() {

    private var showcaseList: List<ShopEtalaseModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShowcaseListImageViewHolder {
        return ShopShowcaseListImageViewHolder(
                parent.inflateLayout(R.layout.item_shop_showcase_list_image)
        )
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ShopShowcaseListImageViewHolder, position: Int) {
        holder.bind(showcaseList[position])
    }

    fun updateShowcaseList(list: List<ShopEtalaseModel>) {
        this.showcaseList = list
        notifyDataSetChanged()
    }
}