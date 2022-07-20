package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R

class EmptyNotificationWithRecomViewHolder constructor(
        itemView: View?
) : AbstractViewHolder<EmptyModel>(itemView) {

    override fun bind(element: EmptyModel) {}

    companion object {
        val LAYOUT = R.layout.item_notification_empty_with_recom
    }
}