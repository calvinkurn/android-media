package com.tokopedia.shop_showcase.shop_showcase_tab.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.databinding.ItemShopShowcaseListImageBinding
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.adapter.viewholder.ShopShowcaseListImageListener
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.adapter.viewholder.ShopShowcaseListImageViewHolder
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.fragment.ShopShowcaseTabInterface

/**
 * Created by Rafli Syam on 12/03/2021
 */
class ShopPageShowcaseListAdapter(
        private val shopShowcaseListImageListener: ShopShowcaseListImageListener,
        private val shopShowcaseTabInterface: ShopShowcaseTabInterface
) : RecyclerView.Adapter<ShopShowcaseListImageViewHolder>() {

    private var showcaseList: List<ShopEtalaseUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopShowcaseListImageViewHolder {
        val binding = ItemShopShowcaseListImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ShopShowcaseListImageViewHolder(
                itemViewBinding = binding,
                listener = shopShowcaseListImageListener,
                shopShowcaseTabInterface = shopShowcaseTabInterface
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
