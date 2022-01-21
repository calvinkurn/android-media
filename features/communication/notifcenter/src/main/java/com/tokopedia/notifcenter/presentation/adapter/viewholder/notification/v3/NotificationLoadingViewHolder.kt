package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R

class NotificationLoadingViewHolder(
        itemView: View?
) : AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_notification_loading_state
    }
}
