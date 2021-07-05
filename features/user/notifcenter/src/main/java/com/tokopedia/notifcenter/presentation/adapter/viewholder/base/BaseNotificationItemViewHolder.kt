package com.tokopedia.notifcenter.presentation.adapter.viewholder.base

import android.graphics.Typeface.BOLD
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.Resources.Green_G200
import com.tokopedia.notifcenter.data.consts.Resources.Green_G600
import com.tokopedia.notifcenter.data.consts.Resources.Neutral_N200
import com.tokopedia.notifcenter.data.consts.Resources.Neutral_N50
import com.tokopedia.notifcenter.data.consts.Resources.Unify_N0
import com.tokopedia.notifcenter.data.state.BottomSheetType
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean.Companion.BUYER_TYPE
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean.Companion.SELLER_TYPE

/**
 * @author : Steven 10/04/19
 */
abstract class BaseNotificationItemViewHolder(
        itemView: View,
        var listener: NotificationItemListener
) : AbstractViewHolder<NotificationItemViewBean>(itemView) {

    protected val container: ConstraintLayout = itemView.findViewById(R.id.container)
    protected val icon: ImageView = itemView.findViewById(R.id.icon)
    protected val type: TextView = itemView.findViewById(R.id.type)
    protected val label: TextView = itemView.findViewById(R.id.label)
    private val time: TextView = itemView.findViewById(R.id.time)

    protected val title: TextView = itemView.findViewById(R.id.title)
    protected val body: TextView = itemView.findViewById(R.id.body)

    override fun bind(element: NotificationItemViewBean) {
        trackImpression(element)
        bindNotificationBackgroundColor(element)
        bindNotificationHeader(element)
        bindNotificationContent(element)
        bindNotificationPayload(element)
        bindOnNotificationClick(element)
    }

    private fun trackImpression(element: NotificationItemViewBean) {
        listener.trackNotificationImpression(element)
    }

    override fun bind(element: NotificationItemViewBean?, payloads: MutableList<Any>) {
        if (element == null || payloads.isEmpty()) return
        when (payloads.first()) {
            PAYLOAD_CHANGE_BACKGROUND -> bindNotificationBackgroundColor(element)
        }
    }

    protected open fun bindNotificationBackgroundColor(element: NotificationItemViewBean) {
        val color: Int = if (element.isRead) {
            MethodChecker.getColor(container.context, Unify_N0)
        } else {
            MethodChecker.getColor(container.context, R.color.notifcenter_dms_unread_notification)
        }
        container.setBackgroundColor(color)
    }

    protected open fun bindNotificationHeader(element: NotificationItemViewBean) {
        time.text = element.time
        type.text = element.sectionTitle
        convertTypeUser(element)
        ImageHandler.loadImage2(icon, element.iconUrl, R.drawable.ic_notifcenter_loading_toped)
    }

    protected open fun bindNotificationContent(element: NotificationItemViewBean) {
        title.text = element.title
        if (element.isLongerContent) {
            var shorten = element.body.take(element.options.contentMaxLonger)
            val inFull = getStringResource(R.string.in_full)
            shorten = "$shorten... $inFull"
            val spannable = SpannableString(shorten)

            val color = getColorResource(Green_G600)
            spannable.setSpan(
                    ForegroundColorSpan(color),
                    shorten.indexOf(inFull),
                    shorten.indexOf(inFull) + inFull.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                    StyleSpan(BOLD),
                    shorten.indexOf(inFull),
                    shorten.indexOf(inFull) + inFull.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            body.text = spannable
        } else {
            body.text = element.body
        }
    }

    abstract fun bindNotificationPayload(element: NotificationItemViewBean)

    protected open fun notificationItemMarkedClick(element: NotificationItemViewBean) {
        listener.itemClicked(element, adapterPosition)
        element.isRead = true
    }

    protected open fun bindOnNotificationClick(element: NotificationItemViewBean) {
        container.setOnClickListener {
            notificationItemMarkedClick(element)
            if (element.isLongerContent) {
                listener.showNotificationDetail(BottomSheetType.LongerContent, element)
            } else {
                RouteManager.route(itemView.context, element.appLink)
            }
        }
    }

    private fun convertTypeUser(element: NotificationItemViewBean) {
        label.hide()
        val labelIndex = element.label

        if (labelIndex == BUYER_TYPE && element.hasShop) {
            getStringResource(R.string.buyer_label).apply {
                label.text = this
                label.setTextColor(getColorResource(Neutral_N200))
                label.visibility = View.VISIBLE
            }

            label.background.let {
                if (it is GradientDrawable) {
                    it.setColor(getColorResource(Neutral_N50))
                }
            }
        } else if (labelIndex == SELLER_TYPE) {
            getStringResource(R.string.seller_label).apply {
                label.text = this
                label.setTextColor(getColorResource(Green_G600))
                label.visibility = View.VISIBLE
            }

            label.background.let {
                if (it is GradientDrawable) {
                    it.setColor(getColorResource(Green_G200))
                }
            }
        }
    }

    private fun getStringResource(stringId: Int): String {
        return itemView.context?.getString(stringId).toEmptyStringIfNull()
    }

    private fun getColorResource(colorId: Int): Int {
        return MethodChecker.getColor(itemView.context, colorId)
    }

    companion object {
        const val PAYLOAD_CHANGE_BACKGROUND = "payload_change_background"
    }
}