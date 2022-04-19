package com.tokopedia.navigation.presentation.adapter.viewholder

import android.content.Context
import androidx.annotation.LayoutRes
import android.view.View
import android.widget.LinearLayout

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.deprecated.label.LabelView
import com.tokopedia.navigation.domain.model.Inbox
import com.tokopedia.navigation.presentation.view.InboxAdapterListener

/**
 * Author errysuprayogi on 13,March,2019
 */
class InboxViewHolder(itemView: View, private val listener: InboxAdapterListener) : AbstractViewHolder<Inbox>(itemView) {
    private val labelView: LabelView
    private val container: LinearLayout
    private val context: Context

    init {
        labelView = itemView.findViewById(R.id.labelview)
        container = itemView.findViewById(R.id.container)
        context = itemView.context
    }


    override fun bind(item: Inbox) {
        labelView.setImageResource(item.icon!!)
        labelView.title = context.getString(item.title!!)
        labelView.setSubTitle(context.getString(item.subtitle!!))
        if (item.totalBadge != null
                && !item.totalBadge.isEmpty()
                && !item.totalBadge.equals("0", ignoreCase = true)) {
            try {
                labelView.setBadgeCounter(Integer.parseInt(item.totalBadge))
            } catch (e: NumberFormatException) {
                //ignore
            }

        } else {
            labelView.setBadgeCounter(0)
        }
        labelView.showRightArrow(false)
        container.setOnClickListener { listener.onItemClickListener(item, adapterPosition) }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_single_notification
    }
}
