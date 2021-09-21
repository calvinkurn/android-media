package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import kotlinx.android.synthetic.main.holder_home_nav_menu.view.*

class HomeNavMenuViewHolder(
        itemView: View,
        private val listener: HomeNavListener
): AbstractViewHolder<HomeNavMenuDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_menu
    }

    override fun bind(element: HomeNavMenuDataModel) {
        itemView.menu_title?.text = element.itemTitle
        itemView.menu_title?.tag = element.id
        if (element.srcIconId != null) {
            itemView.menu_image.visible()
            itemView.menu_image_unify.gone()

            itemView.menu_image.setImage(newIconId = element.srcIconId)
        } else {
            itemView.menu_image.gone()
            itemView.menu_image_unify.visible()

            itemView.menu_image_unify.loadImage(element.srcImage, R.drawable.grey_button_rounded)
        }
        itemView.setOnClickListener {
            listener.onMenuClick(element)
        }

        if (element.notifCount.isNotEmpty()) {
            if(listener.getReviewCounterAbIsUnify()) {
                itemView.menu_notification.setNotification(
                        notif = element.notifCount,
                        notificationType = NotificationUnify.COUNTER_TYPE,
                        colorType = NotificationUnify.COLOR_PRIMARY
                )
                itemView.menu_notification.visibility = View.VISIBLE
            } else {
                itemView.menu_notification_typography.setNotification(element.notifCount, NotificationUnify.COUNTER_TYPE, NotificationUnify.COLOR_PRIMARY)
                itemView.menu_notification_typography.visibility = View.VISIBLE
            }
        } else {
            itemView.menu_notification_typography.visibility = View.GONE
            itemView.menu_notification.visibility = View.GONE
        }
    }
}