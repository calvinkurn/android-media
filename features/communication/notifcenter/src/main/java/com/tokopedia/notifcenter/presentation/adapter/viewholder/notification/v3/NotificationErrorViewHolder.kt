package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener

class NotificationErrorViewHolder(
        itemView: View?,
        private val notificationItemListener: NotificationItemListener?
) : AbstractViewHolder<ErrorNetworkModel>(itemView) {

    private val globalError: GlobalError? = itemView?.findViewById(R.id.global_error_notifcenter)

    override fun bind(element: ErrorNetworkModel) {
        bindClickPrimaryButton()
        bindSecondaryButton()
    }

    private fun bindClickPrimaryButton() {
        globalError?.setActionClickListener {
            notificationItemListener?.refreshPage()
        }
    }

    private fun bindSecondaryButton() {
        globalError?.errorSecondaryAction?.hide()
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_error_view
    }
}