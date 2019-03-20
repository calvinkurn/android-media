package com.tokopedia.notifcenter.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel
import kotlinx.android.synthetic.main.item_notif_center.view.*

/**
 * @author by milhamj on 30/08/18.
 */

class NotifItemViewHolder(val v: View, val viewListener: NotifCenterContract.View)
    : AbstractViewHolder<NotifItemViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.item_notif_center
    }

    override fun bind(element: NotifItemViewModel?) {
        element?.run {
            ImageHandler.loadImage2(itemView.imageView, this.image, R.drawable.ic_loading_image)
            itemView.timeSummary.text = this.timeSummary
            itemView.section.text = this.section
            itemView.time.text = this.time
            itemView.title.text = MethodChecker.fromHtml(this.title)
            itemView.mainLayout.setBackgroundColor(
                    if (this.readStatus == NotifItemViewModel.READ_STATUS_TRUE)
                        MethodChecker.getColor(itemView.context, R.color.white)
                    else
                        MethodChecker.getColor(itemView.context, R.color.notif_unread_color)
            )
            itemView.mainLayout.setOnClickListener {
                viewListener.openRedirectUrl(this.redirectLink, element.notifId)
            }
            itemView.timeSummary.visibility = if (this.showTimeSummary) View.VISIBLE else View.GONE
        }

    }


}
