package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.getDayDiffFromToday
import com.tokopedia.utils.date.toString
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import com.tokopedia.gamification.R as gamificationR

class KetupatTopBannerVH(itemView: View) : AbstractViewHolder<KetupatTopBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_top_banner

        const val TIMER_DATE_FORMAT = "yyyy-MM-dd"
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
//            val diff = parseData(it.startTime, TIMER_DATE_FORMAT)?.getDayDiffFromToday
            val diff = getDateDiffFromToday(it.startTime)
            if (diff != null) {
                if (diff in 2..7) {
                    itemView.findViewById<IconUnify>(gamificationR.id.ic_clock).show()
                    itemView.findViewById<Typography>(gamificationR.id.top_banner_counter).apply {
                        text = "$diff Days"
                        show()
                    }
                } else {
                    val date = formatDate("yyyy-MM-dd hh:mm:ss Z", "hh", it.startTime + "00")
                    itemView.findViewById<IconUnify>(gamificationR.id.ic_clock).show()
                    itemView.findViewById<Typography>(gamificationR.id.top_banner_counter).apply {
                        text = date
                        show()
                    }
                }
            }
        }
    }

    private fun getDateDiffFromToday(date: String?): Long? {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z", Locale.ENGLISH)
            formatter.isLenient = false
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.parse(date + "00")?.getDayDiffFromToday()?.absoluteValue
        } catch (e: ParseException) {
            Timber.e(e)
        }

        return -1
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
