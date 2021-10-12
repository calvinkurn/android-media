package com.tokopedia.topchat.chatlist.adapter.viewholder.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.data.TopchatItemMenu

class LongClickMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var icon: IconUnify? = itemView.findViewById(R.id.ivIcon)
    private var title: TextView? = itemView.findViewById(R.id.tvTitle)

    fun bind(menu: TopchatItemMenu, onClick: ((TopchatItemMenu, Int) -> Unit)?) {
        bindStartIcon(menu)
        bindTitle(menu)
        bindClickListener(menu, onClick)
    }

    private fun bindStartIcon(menu: TopchatItemMenu) {
        val drawable = ContextCompat.getDrawable(itemView.context, menu.icon) ?: return
        val colorLightEnable = ContextCompat.getColor(
            itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900
        )
        applyIconUnifyColor(drawable, colorLightEnable)
        icon?.setImageDrawable(drawable)
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