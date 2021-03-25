package com.tokopedia.shop.showcase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageListener
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageViewHolder

/**
 * Created by Rafli Syam on 12/03/2021
 */
class ShopPageShowcaseListAdapter(
        private val shopShowcaseListImageListener: ShopShowcaseListImageListener
) : RecyclerView.Adapter<ShopShowcaseListImageViewHolder>() {

    private var showcaseList: List<ShopEtalaseModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShowcaseListImageViewHolder {
        return ShopShowcaseListImageViewHolder(
                itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_shop_showcase_list_image,
                        parent,
                        false
                ),
                listener = shopShowcaseListImageListener
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