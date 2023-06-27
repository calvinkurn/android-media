package com.tokopedia.notifcenter.ui.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R

class NotificationLoadMoreViewHolder(
        itemView: View?
) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    override fun bind(element: LoadingMoreModel?) { }

    companion object {
        val LAYOUT = R.layout.item_notification_load_more
    }
}
