package com.tokopedia.topchat.chatlist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.component.Menus
import com.tokopedia.topchat.chatlist.adapter.viewholder.menu.LongClickMenuViewHolder

class LongClickMenuAdapter : RecyclerView.Adapter<LongClickMenuViewHolder>() {

    var onClick: ((Menus.ItemMenus, Int) -> Unit)? = null
    var menus: MutableList<Menus.ItemMenus> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LongClickMenuViewHolder {
        return LongClickMenuViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: LongClickMenuViewHolder, position: Int) {
        holder.bind(menus[position], onClick)
    }

    fun setOnItemMenuClickListener(onClick: (Menus.ItemMenus, Int) -> Unit) {
        this.onClick = onClick
    }

}