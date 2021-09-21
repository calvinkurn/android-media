package com.tokopedia.shop.showcase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageBaseViewHolder
import com.tokopedia.shop.showcase.presentation.adapter.viewholder.ShopShowcaseListImageListener
import com.tokopedia.shop.showcase.presentation.adapter.viewholder.ShopShowcaseListImageViewHolder

/**
 * Created by Rafli Syam on 12/03/2021
 */
class ShopPageShowcaseListAdapter(
        private val shopShowcaseListImageListener: ShopShowcaseListImageListener
) : RecyclerView.Adapter<ShopShowcaseListImageViewHolder>() {

    private var showcaseList: List<ShopEtalaseUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShowcaseListImageViewHolder {
        return ShopShowcaseListImageViewHolder(
                itemView = LayoutInflater.from(parent.context).inflate(
                        ShopShowcaseListImageBaseViewHolder.LAYOUT,
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

    fun updateShowcaseList(list: List<ShopEtalaseUiModel>) {
        this.showcaseList = list
        notifyDataSetChanged()
    }
}