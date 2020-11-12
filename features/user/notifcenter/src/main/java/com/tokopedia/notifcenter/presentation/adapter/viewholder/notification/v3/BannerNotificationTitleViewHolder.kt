package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.inboxcommon.time.TimeHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography

class BannerNotificationTitleViewHolder(
        itemView: View?,
        private val listener: NotificationItemListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val banner: ImageView? = itemView?.findViewById(R.id.iv_banner)
    private val status: Typography? = itemView?.findViewById(R.id.tv_status)
    private val endDate: Typography? = itemView?.findViewById(R.id.tv_end_date)
    private val countDown: TimerUnifySingle? = itemView?.findViewById(R.id.tu_countdown)
    private val imageSizer = DynamicSizeImageRequestListener()

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindBannerImage(element)
        bindFooterTimeStatus(element)
        bindClickBanner(element)
    }

    override fun onViewRecycled() {
        countDown?.timer?.cancel()
    }

    private fun bindBannerImage(element: NotificationUiModel) {
        ImageHandler.loadImageWithListener(
                banner, element.dataNotification.infoThumbnailUrl, imageSizer
        )
    }

    private fun bindFooterTimeStatus(element: NotificationUiModel) {
        val isIn24HourAfterCurrentTime = TimeHelper.isIn24HourAfterCurrentTime(
                element.expireTimeUnix
        )
        val isAfterCurrentTime = TimeHelper.isAfterCurrentTime(element.expireTimeUnix)
        val isBeforeCurrentTime = TimeHelper.isBeforeCurrentTime(element.expireTimeUnix)
        val timeMetaData = TimeMetaData(
                isIn24HourAfterCurrentTime, isAfterCurrentTime, isBeforeCurrentTime
        )
        bindFooterWithTimeMetaData(element, timeMetaData)
    }

    private fun bindFooterTimeStatusHasEnded(element: NotificationUiModel) {
        val timeMetaData = TimeMetaData(
                isIn24HourAfterCurrentTime = false,
                isAfterCurrentTime = false,
                isBeforeCurrentTime = true
        )
        bindFooterWithTimeMetaData(element, timeMetaData)
    }

    private fun bindFooterWithTimeMetaData(
            element: NotificationUiModel, timeMetaData: TimeMetaData
    ) {
        bindState(element, timeMetaData)
        bindEndDate(element, timeMetaData)
        bindCountDown(element, timeMetaData)
    }

    private fun bindState(element: NotificationUiModel, timeMetaData: TimeMetaData) {
        @StringRes
        val text: Int? = when {
            timeMetaData.isIn24HourAfterCurrentTime -> R.string.title_notifcenter_end_in
            timeMetaData.isAfterCurrentTime -> R.string.title_notifcenter_end_until
            timeMetaData.isBeforeCurrentTime -> R.string.title_notifcenter_promo_already_ended
            else -> null
        }
        text?.let {
            status?.setText(it)
        }
    }

    private fun bindEndDate(element: NotificationUiModel, timeMetaData: TimeMetaData) {
        val text: String? = when {
            // hide if it's end in 24 hour, need to show countdown
            timeMetaData.isIn24HourAfterCurrentTime -> null
            timeMetaData.isAfterCurrentTime -> TimeHelper.getDateMonthYearFormat(
                    element.expireTimeUnix * 1000
            )
            else -> null
        }
        if (text != null) {
            endDate?.show()
            endDate?.text = text
        } else {
            endDate?.hide()
        }
    }

    private fun bindCountDown(element: NotificationUiModel, timeMetaData: TimeMetaData) {
        if (timeMetaData.isIn24HourAfterCurrentTime) {
            countDown?.show()
            showCountDownTimer(element)
        } else {
            countDown?.hide()
        }
    }

    private fun showCountDownTimer(element: NotificationUiModel) {
        countDown?.targetDate = element.expireTargetDate
        countDown?.onFinish = {
            bindFooterTimeStatusHasEnded(element)
        }
    }

    private fun bindClickBanner(element: NotificationUiModel) {
        container?.setOnClickListener {
            listener?.showLongerContent(element)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_banner_notification
    }

    data class TimeMetaData(
            val isIn24HourAfterCurrentTime: Boolean,
            val isAfterCurrentTime: Boolean,
            val isBeforeCurrentTime: Boolean
    )
}