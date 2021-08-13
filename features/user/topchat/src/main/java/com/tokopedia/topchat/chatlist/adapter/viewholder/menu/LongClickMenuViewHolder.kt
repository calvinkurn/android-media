package com.tokopedia.topchat.chatlist.adapter.viewholder.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.data.TopchatItemMenu

class LongClickMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var icon: ImageView? = itemView.findViewById(R.id.ivIcon)
    private var title: TextView? = itemView.findViewById(R.id.tvTitle)

    fun bind(menu: TopchatItemMenu, onClick: ((TopchatItemMenu, Int) -> Unit)?) {
        bindStartIcon(menu)
        bindTitle(menu)
        bindClickListener(menu, onClick)
    }

    private fun bindStartIcon(menu: TopchatItemMenu) {
        icon?.setImageResource(menu.icon)
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
        fun create(parent: ViewGroup, viewType: Int): LongClickMenuViewHolder {
            return LongClickMenuViewHolder(LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false))
        }

        val LAYOUT = R.layout.item_menu_long_click_topchat
    }
}