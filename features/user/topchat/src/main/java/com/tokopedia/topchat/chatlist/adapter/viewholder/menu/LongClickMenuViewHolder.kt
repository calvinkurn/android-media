package com.tokopedia.topchat.chatlist.adapter.viewholder.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.data.TopchatItemMenu
import com.tokopedia.unifycomponents.NotificationUnify

class LongClickMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var icon: IconUnify? = itemView.findViewById(R.id.ivIcon)
    private var title: TextView? = itemView.findViewById(R.id.tvTitle)
    private var newLabel: NotificationUnify? = itemView.findViewById(R.id.nu_new_label)

    fun bind(menu: TopchatItemMenu, onClick: ((TopchatItemMenu, Int) -> Unit)?) {
        bindStartIcon(menu)
        bindTitle(menu)
        bindClickListener(menu, onClick)
        bindNewLabel(menu)
    }

    private fun bindNewLabel(menu: TopchatItemMenu) {
        newLabel?.showWithCondition(menu.showNewLabel)
    }

    private fun bindStartIcon(menu: TopchatItemMenu) {
        val drawable = if (menu.iconUnify != null) {
            getIconUnifyDrawable(itemView.context, menu.iconUnify)
        } else {
            ContextCompat.getDrawable(itemView.context, menu.icon)
        } ?: return
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