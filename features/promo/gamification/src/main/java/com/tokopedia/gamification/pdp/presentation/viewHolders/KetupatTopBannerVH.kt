package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.relativeDate
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toDate
import com.tokopedia.recommendation_widget_common.extension.mappingMiniCartDataToRecommendation
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.getDayDiffFromToday
import com.tokopedia.utils.date.removeTime
import com.tokopedia.utils.date.toString
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.tokopedia.gamification.R as gamificationR

class KetupatTopBannerVH(itemView: View) : AbstractViewHolder<KetupatTopBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_top_banner

        const val TIMER_DATE_FORMAT = "yyyy-MM-dd"

        fun parseData(date: String?, timerFormat: String = TIMER_DATE_FORMAT): Date? {
            return date?.let {
                try {
                    SimpleDateFormat(timerFormat, Locale.getDefault())
                        .parse(date)
                } catch (parseException: ParseException) {
                    null
                }
            }
        }
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
            val diff = parseData(it.startTime, TIMER_DATE_FORMAT)?.getDayDiffFromToday()
            if (diff != null) {
                if (diff < 0) {
                    itemView.findViewById<Typography>(gamificationR.id.top_banner_counter).hide()
                    itemView.findViewById<IconUnify>(gamificationR.id.ic_clock).hide()
                }
                if (diff <= 7) {
                    itemView.findViewById<Typography>(gamificationR.id.top_banner_counter).text =
                        "$diff Days"
                } else {
                    val date = parseData(it.startTime, TIMER_DATE_FORMAT)?.time?.toDate().toString()
                    itemView.findViewById<Typography>(gamificationR.id.top_banner_counter).text =
                        date.subSequence(0,11)
                }
            }
        }
    }

}
