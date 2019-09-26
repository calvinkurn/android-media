package com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate

import android.graphics.drawable.GradientDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel.Companion.BUYER_TYPE
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel.Companion.SELLER_TYPE


/**
 * @author : Steven 10/04/19
 */
abstract class NotificationUpdateItemViewHolder(itemView: View, var listener: NotificationUpdateItemListener)
    : AbstractViewHolder<NotificationUpdateItemViewModel>(itemView) {

    protected val container: ConstraintLayout = itemView.findViewById(R.id.container)
    protected val icon: ImageView = itemView.findViewById(R.id.icon)
    protected val type: TextView = itemView.findViewById(R.id.type)
    protected val time: TextView = itemView.findViewById(R.id.time)
    protected val label: TextView = itemView.findViewById(R.id.label)

    protected val title: TextView = itemView.findViewById(R.id.title)
    protected val body: TextView = itemView.findViewById(R.id.body)

    override fun bind(element: NotificationUpdateItemViewModel) {
        bindNotificationBackgroundColor(element)
        bindNotificationHeader(element)
        bindNotificationContent(element)
        bindNotificationPayload(element)
        bindOnNotificationClick(element)
    }

    override fun bind(element: NotificationUpdateItemViewModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.isEmpty()) return
        when (payloads[0]) {
            PAYLOAD_CHANGE_BACKGROUND -> bindNotificationBackgroundColor(element)
        }
    }

    protected open fun bindNotificationBackgroundColor(element: NotificationUpdateItemViewModel) {
        val color: Int = if (element.isRead) {
            MethodChecker.getColor(container.context, R.color.white)
        } else {
            MethodChecker.getColor(container.context, R.color.Green_G100)
        }
        container.setBackgroundColor(color)
    }

    protected open fun bindNotificationHeader(element: NotificationUpdateItemViewModel) {
        time.text = element.time
        type.text = element.sectionTitle
        convertTypeUser(element)
        ImageHandler.loadImage2(icon, element.iconUrl, R.drawable.ic_loading_toped_new)
    }

    protected open fun bindNotificationContent(element: NotificationUpdateItemViewModel) {
        title.text = element.title
        body.text = element.body
    }

    abstract fun bindNotificationPayload(element: NotificationUpdateItemViewModel)

    protected open fun bindOnNotificationClick(element: NotificationUpdateItemViewModel) {
        container.setOnClickListener {
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
            if (element.body.length > 110) {
                listener.showTextLonger(element)
            } else {
                RouteManager.route(itemView.context, element.appLink)
            }
        }
    }

    private fun convertTypeUser(element: NotificationUpdateItemViewModel) {
        label.visibility = View.GONE
        val labelIndex = element.label

        if (labelIndex == BUYER_TYPE && element.hasShop) {
            getStringResource(R.string.buyer_label)?.apply {
                label.text = this
                label.setTextColor(getColorResource(R.color.Neutral_N200))
                label.visibility = View.VISIBLE
            }

            label.background.let {
                if (it is GradientDrawable) {
                    it.setColor(getColorResource(R.color.Neutral_N50))
                }
            }
        } else if (labelIndex == SELLER_TYPE) {
            getStringResource(R.string.seller_label)?.apply {
                label.text = this
                label.setTextColor(getColorResource(R.color.Green_G500))
                label.visibility = View.VISIBLE
            }

            label.background.let {
                if (it is GradientDrawable) {
                    it.setColor(getColorResource(R.color.Green_G200))
                }
            }
        }
    }

    private fun getStringResource(stringId: Int): String? {
        return itemView.context?.getString(stringId)
    }

    private fun getColorResource(colorId: Int): Int {
        return MethodChecker.getColor(itemView.context, colorId)
    }

    companion object {
        val PAYLOAD_CHANGE_BACKGROUND = "payload_change_background"
        val MAX_CONTENT_LENGTH = 110
    }
}