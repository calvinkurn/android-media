package com.tokopedia.navigation.presentation.adapter.viewholder

import android.content.Context
import androidx.annotation.LayoutRes
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.IconUnify.Companion.CHEVRON_RIGHT
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.Inbox
import com.tokopedia.navigation.presentation.view.InboxAdapterListener
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Author errysuprayogi on 13,March,2019
 */
class InboxViewHolder(itemView: View, private val listener: InboxAdapterListener) : AbstractViewHolder<Inbox>(itemView) {

    private val menuLayout: ConstraintLayout? = itemView.findViewById(R.id.layout_single_menu_inbox_home)
    private val iconMenu: IconUnify? = itemView.findViewById(R.id.icon_menu_inbox_home)
    private val iconChevron: IconUnify? = itemView.findViewById(R.id.icon_chevron_right_inbox_home)
    private val notification: NotificationUnify? = itemView.findViewById(R.id.notification_counter_inbox_home)
    private val menuTitle: Typography? = itemView.findViewById(R.id.tv_title_menu_inbox_home)
    private val menuDesc: Typography? = itemView.findViewById(R.id.tv_desc_menu_inbox_home)
    private val context: Context? = itemView.context

    override fun bind(item: Inbox) {
        bindViews(item)
        bindCounter(item)
        bindListener(item)
    }

    private fun bindViews(item: Inbox) {
        iconMenu?.setImage(item.icon)
        iconChevron?.setImage(CHEVRON_RIGHT)
        menuTitle?.text = context?.getString(item.title)
        menuDesc?.text = context?.getString(item.subtitle)
    }

    private fun bindCounter(item: Inbox) {
        if (item.totalBadge != null &&
            item.totalBadge.isNotEmpty() &&
            !item.totalBadge.equals("0", ignoreCase = true)
        ) {
            notification?.setNotification(
                item.totalBadge,
                NotificationUnify.TEXT_TYPE,
                NotificationUnify.COLOR_PRIMARY
            )
            notification?.show()
        } else {
            notification?.hide()
        }
    }

    private fun bindListener(item: Inbox) {
        menuLayout?.setOnClickListener {
            listener.onItemClickListener(item, adapterPosition)
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_single_menu_inbox
    }
}
