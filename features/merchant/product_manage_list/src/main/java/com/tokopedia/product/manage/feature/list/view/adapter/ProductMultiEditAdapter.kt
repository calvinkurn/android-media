package com.tokopedia.product.manage.feature.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.MultiEditViewHolder

class ProductMultiEditAdapter(
    private val listener: MultiEditViewHolder.MenuClickListener,
    private val isShopModerated: Boolean
) : RecyclerView.Adapter<MultiEditViewHolder>() {

    var menuList: List<Int> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiEditViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(MultiEditViewHolder.LAYOUT, parent, false)
        return MultiEditViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = menuList.count()

    override fun onBindViewHolder(holder: MultiEditViewHolder, position: Int) {
        holder.bind(menuList[position], isShopModerated)
    }
}
