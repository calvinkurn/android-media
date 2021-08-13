package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.Ratio
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.widget.BroadcastBannerNotificationImageView
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.time.TimeHelper

class BannerNotificationTitleViewHolder(
        itemView: View?,
        private val listener: NotificationItemListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val banner: BroadcastBannerNotificationImageView? = itemView?.findViewById(
            R.id.iv_banner
    )
    private val status: Typography? = itemView?.findViewById(R.id.tv_status)
    private val endDate: Typography? = itemView?.findViewById(R.id.tv_end_date)
    private val countDown: TimerUnifySingle? = itemView?.findViewById(R.id.tu_countdown)

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindBannerImage(element)
        bindFooterTimeStatus(element)
    }

    override fun onViewRecycled() {
        countDown?.timer?.cancel()
        ImageHandler.clearImage(banner)
    }

    private fun bindBannerImage(element: NotificationUiModel) {
        // workaround Glide issue with dynamic height size
        // https://github.com/bumptech/glide/issues/835#issuecomment-167438903
        banner?.layout(0, 0, 0, 0)
        val imageRatio = element.imageMetaData.getOrNull(0)?.ratio ?: Ratio()
        banner?.ratio = (imageRatio.y / imageRatio.x)
        ImageHandler.loadImageRounded(
                itemView.context, banner, element.dataNotification.infoThumbnailUrl, bannerRadius
        )
    }

    private fun bindFooterTimeStatus(element: NotificationUiModel) {
        if (element.isPromotion()) {
            val isIn24HourAfterCurrentTime = TimeHelper.isIn24HourAfterCurrentTime(
                    element.expireTimeUnixMillis
            )
            val isAfterCurrentTime = TimeHelper.isAfterCurrentTime(element.expireTimeUnixMillis)
            val isBeforeCurrentTime = TimeHelper.isBeforeCurrentTime(element.expireTimeUnixMillis)
            val timeMetaData = TimeMetaData(
                    isIn24HourAfterCurrentTime, isAfterCurrentTime, isBeforeCurrentTime
            )
            bindFooterWithTimeMetaData(element, timeMetaData)
        } else {
            status?.hide()
            endDate?.hide()
            countDown?.hide()
        }
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
            status?.show()
            status?.setText(it)
        }
    }

    private fun bindEndDate(element: NotificationUiModel, timeMetaData: TimeMetaData) {
        val text: String? = when {
            // hide if it's end in 24 hour, need to show countdown
            timeMetaData.isIn24HourAfterCurrentTime -> null
            timeMetaData.isAfterCurrentTime -> TimeHelper.getDateMonthYearFormat(
                    element.expireTimeUnixMillis
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

    companion object {
        val LAYOUT = R.layout.item_notifcenter_banner_notification
        private val bannerRadius = 8f.toPx()
    }

    data class TimeMetaData(
            val isIn24HourAfterCurrentTime: Boolean,
            val isAfterCurrentTime: Boolean,
            val isBeforeCurrentTime: Boolean
    )
}