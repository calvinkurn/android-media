package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.databinding.ItemShopShowcaseListImageBinding
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageBaseViewHolder
import com.tokopedia.shop_showcase.common.ShopShowcaseManagementListener

class ShopShowcaseListAdapter(
        val listener: ShopShowcaseManagementListener,
        val isMyShop: Boolean
) : RecyclerView.Adapter<ShopShowcaseListAdapter.ViewHolder>() {

    private var showcaseList: MutableList<ShopEtalaseModel> = mutableListOf()

    fun updateDataShowcaseList(showcaseListData: List<ShopEtalaseModel>) {
        showcaseList = showcaseListData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopShowcaseListImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(showcaseList[position])
    }

    inner class ViewHolder(itemViewBinding: ItemShopShowcaseListImageBinding) : ShopShowcaseListImageBaseViewHolder(itemViewBinding) {

        init {
            itemViewBinding.apply {
                itemShowcaseActionButton = imgMenuMore
            }
        }

        override fun bind(element: Any) {
            // cast to actual ui model
            val elementUiModel = element as ShopEtalaseModel

            // render showcase info
            renderShowcaseMainInfo(elementUiModel, isMyShop)

            // handle item view showcase click
            setItemShowcaseClickListener {
                listener.sendClickShowcase(elementUiModel, adapterPosition)
            }

            // set listener for showcase action button
            itemShowcaseActionButton?.apply {
                // action button click listener
                setOnClickListener {
                    listener.sendClickShowcaseMenuMore(elementUiModel, adapterPosition)
                }
            }
        }
    }
}