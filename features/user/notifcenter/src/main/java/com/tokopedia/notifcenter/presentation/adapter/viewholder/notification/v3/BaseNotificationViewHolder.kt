package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper

abstract class BaseNotificationViewHolder constructor(
        itemView: View?,
        private val listener: NotificationItemListener?
) : AbstractViewHolder<NotificationUiModel>(itemView) {

    protected val container: ConstraintLayout? = itemView?.findViewById(R.id.notification_container)
    protected val icon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    protected val type: Typography? = itemView?.findViewById(R.id.txt_notification_type)
    protected val time: Typography? = itemView?.findViewById(R.id.txt_notification_time)
    protected val title: Typography? = itemView?.findViewById(R.id.txt_notification_title)
    protected val desc: Typography? = itemView?.findViewById(R.id.txt_notification_desc)

    protected val clickedColor = MethodChecker.getColor(
            itemView?.context, R.color.notifcenter_dms_unread_notification
    )

    override fun bind(element: NotificationUiModel) {
        bindContainer(element)
        bindTitle(element)
        bindDesc(element)
        bindNotificationType(element)
        bindIcon(element)
        bindTime(element)
        bindClick(element)
        trackSeenNotification(element)
    }

    protected open fun bindClick(element: NotificationUiModel) {
        container?.setOnClickListener {
            markAsReadIfUnread(element)
            if (isLongerContent(element)) {
                showLongerContent(element)
            } else {
                RouteManager.route(itemView.context, element.dataNotification.appLink)
            }
        }
    }

    private fun trackSeenNotification(element: NotificationUiModel) {
        if (!element.hasBeenSeen()) {
            listener?.markAsSeen(element.notifId)
        }
    }

    protected open fun showLongerContent(element: NotificationUiModel) {
        listener?.showLongerContent(element)
    }

    protected open fun isLongerContent(element: NotificationUiModel): Boolean {
        return element.isLongerContent
    }

    protected open fun bindContainer(element: NotificationUiModel) {
        if (!element.isRead()) {
            container?.setBackgroundColor(clickedColor)
        } else {
            container?.background = null
        }
    }

    protected fun markAsReadIfUnread(element: NotificationUiModel) {
        if (!element.isRead()) {
            markNotificationAsRead(element)
        }
    }

    protected fun markNotificationAsRead(element: NotificationUiModel) {
        element.markNotificationAsRead()
        bindContainer(element)
        listener?.markNotificationAsRead(element)
    }

    private fun bindTitle(element: NotificationUiModel) {
        title?.text = element.title
    }

    private fun bindDesc(element: NotificationUiModel) {
        if (element.isLongerContent) {
            var shorten = element.shortDescription.take(
                    element.options.longerContent
            )
            val inFull = getStringResource(R.string.in_full)
            shorten = "$shorten... $inFull"
            val spannable = SpannableString(shorten)

            val color = getColorResource(com.tokopedia.unifyprinciples.R.color.Unify_G500)
            spannable.setSpan(
                    ForegroundColorSpan(color),
                    shorten.indexOf(inFull),
                    shorten.indexOf(inFull) + inFull.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    shorten.indexOf(inFull),
                    shorten.indexOf(inFull) + inFull.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            desc?.text = spannable
        } else {
            desc?.text = element.shortDescription
        }
    }

    private fun bindNotificationType(element: NotificationUiModel) {
        type?.text = element.sectionKey
    }

    private fun bindIcon(element: NotificationUiModel) {
        icon?.let {
            ImageHandler.LoadImage(icon, element.sectionIcon)
        }
    }

    private fun bindTime(element: NotificationUiModel) {
        time?.text = TimeHelper.getRelativeTimeFromNow(element.createTimeUnixMillis)
    }

    private fun getStringResource(stringId: Int): String {
        return itemView.context?.getString(stringId).toEmptyStringIfNull()
    }

    private fun getColorResource(colorId: Int): Int {
        return MethodChecker.getColor(itemView.context, colorId)
    }
}