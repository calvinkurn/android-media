package com.tokopedia.topchat.chatlist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatlist.adapter.viewholder.menu.FilterMenuViewHolder
import com.tokopedia.topchat.common.data.TopchatItemMenu

class FilterMenuAdapter : RecyclerView.Adapter<FilterMenuViewHolder>() {

    var onClick: ((TopchatItemMenu, Int) -> Unit)? = null
    var menus: MutableList<TopchatItemMenu> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMenuViewHolder {
        return FilterMenuViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: FilterMenuViewHolder, position: Int) {
        holder.bind(menus[position], onClick)
    }

    fun setOnItemMenuClickListener(onClick: (TopchatItemMenu, Int) -> Unit) {
        this.onClick = onClick
    }

}