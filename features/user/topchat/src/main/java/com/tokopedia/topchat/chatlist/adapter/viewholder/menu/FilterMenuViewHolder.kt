package com.tokopedia.topchat.chatlist.adapter.viewholder.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.data.TopchatItemMenu

class FilterMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var icon: ImageView? = itemView.findViewById(R.id.ivIcon)
    private var title: TextView? = itemView.findViewById(R.id.tvTitle)

    fun bind(menu: TopchatItemMenu, onClick: ((TopchatItemMenu, Int) -> Unit)?) {
        bindCheckIcon(menu)
        bindTitle(menu)
        bindClickListener(menu, onClick)
    }

    private fun bindCheckIcon(menu: TopchatItemMenu) {
        icon?.showWithCondition(menu.iconEnd > 0)
    }

    private fun bindTitle(menu: TopchatItemMenu) {
        title?.text = menu.title
    }

    private fun bindClickListener(menu: TopchatItemMenu, onClick: ((TopchatItemMenu, Int) -> Unit)?) {
        itemView.setOnClickListener {
            onClick?.let { click -> click(menu, adapterPosition) }
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): FilterMenuViewHolder {
            return FilterMenuViewHolder(LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false))
        }

        val LAYOUT = R.layout.item_menu_filter_topchat
    }
}