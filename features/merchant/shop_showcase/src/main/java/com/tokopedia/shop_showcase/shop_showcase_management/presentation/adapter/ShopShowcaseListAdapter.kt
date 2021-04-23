package com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.common.R
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
        return ViewHolder(parent.inflateLayout(
                ShopShowcaseListImageBaseViewHolder.LAYOUT
        ))
    }

    override fun getItemCount(): Int {
        return showcaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(showcaseList[position])
    }

    inner class ViewHolder(itemView: View) : ShopShowcaseListImageBaseViewHolder(itemView) {

        init {
            showcaseActionButton = itemView.findViewById(R.id.img_menu_more)
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
            showcaseActionButton?.apply {
                // action button click listener
                setOnClickListener {
                    listener.sendClickShowcaseMenuMore(elementUiModel, adapterPosition)
                }
            }
        }
    }
}