package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.getDayDiffFromToday
import com.tokopedia.utils.date.trimDate
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.tokopedia.gamification.R as gamificationR

class KetupatTopBannerVH(itemView: View) : AbstractViewHolder<KetupatTopBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_top_banner

    }

    override fun bind(element: KetupatTopBannerVHModel?) {
        element?.header?.let { header ->
            itemView.findViewById<ImageUnify>(gamificationR.id.header_image)
                .apply {
                    this.cornerRadius = 0
                }
                ?.setImageUrl(header.assets.find { it?.key == "BACKGROUND_IMAGE" }?.value.toString())
        }
        element?.scratchCard?.let {
            setDate(it.startTime, it.endTime)
            GamificationAnalytics.sendImpressHeaderSectionEvent("direct_reward_id: ${it.id.toString()}")
        }
    }

    private fun setDate(startTime: String?, endTime: String?) {
        val diffStartTime = getDateDiffFromToday(startTime)
        val diffEndTime = getDateDiffFromToday(endTime)
        val diff: Long
        val time: String
        if ((diffStartTime ?: 0) < 0) {
            diff = diffEndTime!!
            time = endTime.toString()
            itemView.findViewById<Typography>(gamificationR.id.top_banner_subtitle).text =
                "Event berakhir"
        } else {
            diff = diffStartTime ?: 0
            time = startTime.toString()
        }
        itemView.findViewById<IconUnify>(gamificationR.id.ic_clock).show()
        val date = when {
            diff > 7 -> {
                formatDate("yyyy-MM-dd HH:mm:ss Z", "dd MMMM yyyy", time + "00")
            }

            diff in 1..7 -> {
                "${diff} Hari lagi"
            }

            diff < 0 -> {
                itemView.findViewById<IconUnify>(gamificationR.id.ic_clock).hide()
                ""
            }
            else -> {
                itemView.findViewById<IconUnify>(gamificationR.id.ic_clock).hide()
                itemView.findViewById<IconUnify>(gamificationR.id.top_banner_counter).hide()
                showTimer(time)
                ""
            }
        }
        itemView.findViewById<Typography>(gamificationR.id.top_banner_counter).apply {
            text = date
            show()
        }
    }

    private fun showTimer(time: String) {
        itemView.findViewById<TimerUnifySingle>(gamificationR.id.top_banner_timer).apply {
            this.visible()
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.time = getAbsoluteDiff(time)
            this.targetDate = calendar
        }
    }

    private fun getDateDiffFromToday(date: String?): Long? {
        try {
            val localeID = Locale("in", "ID")
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", localeID)
            formatter.isLenient = false
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.parse(date + "00")?.getDayDiffFromToday()
        } catch (e: ParseException) {
            Timber.e(e)
        }
        return -1
    }

    private fun getAbsoluteDiff(time: String): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)
        formatter.isLenient = false
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.parse(time + "00")
    }

    private fun formatDate(
        currentFormat: String,
        newFormat: String,
        dateString: String
    ): String {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, Locale.ENGLISH)
            fromFormat.isLenient = false
            fromFormat.timeZone = TimeZone.getTimeZone("UTC")
            val toFormat: DateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
            toFormat.isLenient = false
            toFormat.timeZone = TimeZone.getDefault()

            val date = fromFormat.parse(dateString)
            date?.let { toFormat.format(it) } ?: String.EMPTY
        } catch (e: ParseException) {
            Timber.e(e, "cannot be parsed")
            dateString
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Pattern is invalid")
            dateString
        }
    }
}
