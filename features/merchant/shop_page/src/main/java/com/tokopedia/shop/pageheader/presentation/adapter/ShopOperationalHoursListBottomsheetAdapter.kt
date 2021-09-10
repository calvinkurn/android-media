package com.tokopedia.shop.pageheader.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Rafli Syam on 16/04/2021
 */
class ShopOperationalHoursListBottomsheetAdapter(
        private val context: Context?,
        private val operationalHoursList: MutableList<ShopOperationalHour>
): RecyclerView.Adapter<ShopOperationalHoursListBottomsheetAdapter.ViewHolder>() {

    companion object {
        @LayoutRes
        private val ITEM_LAYOUT = R.layout.item_shop_operational_hours

        // Days Name List
        const val MONDAY_LABEL = "Senin"
        const val TUESDAY_LABEL = "Selasa"
        const val WEDNESDAY_LABEL = "Rabu"
        const val THURSDAY_LABEL = "Kamis"
        const val FRIDAY_LABEL = "Jumat"
        const val SATURDAY_LABEL = "Sabtu"
        const val SUNDAY_LABEL = "Minggu"

        // Days Number List
        const val MONDAY_NUMBER = 1
        const val TUESDAY_NUMBER = 2
        const val WEDNESDAY_NUMBER = 3
        const val THURSDAY_NUMBER = 4
        const val FRIDAY_NUMBER = 5
        const val SATURDAY_NUMBER = 6
        const val SUNDAY_NUMBER = 7

        // Time Constant
        const val START_TIME_INDEX = 0
        const val END_TIME_INDEX = 5
        const val MIN_START_TIME = "00:00:00"
        const val MAX_END_TIME = "23:59:00"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(context, ITEM_LAYOUT, null))
    }

    override fun getItemCount(): Int {
        return operationalHoursList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(operationalHoursList[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var dayNameMap: Map<Int, String> = mapOf(
                MONDAY_NUMBER to MONDAY_LABEL,
                TUESDAY_NUMBER to TUESDAY_LABEL,
                WEDNESDAY_NUMBER to WEDNESDAY_LABEL,
                THURSDAY_NUMBER to THURSDAY_LABEL,
                FRIDAY_NUMBER to FRIDAY_LABEL,
                SATURDAY_NUMBER to SATURDAY_LABEL,
                SUNDAY_NUMBER to SUNDAY_LABEL
        )
        private var tvOperationalDay: Typography? = null
        private var tvOperationalDateTime: Typography? = null
        private var itemDivider: View? = null

        init {
            tvOperationalDay = itemView.findViewById(R.id.tvOperationalDay)
            tvOperationalDateTime = itemView.findViewById(R.id.tvOperationalDateTime)
            itemDivider = itemView.findViewById(R.id.item_separator_ops_hour)
        }

        fun bind(element: ShopOperationalHour) {
            tvOperationalDay?.text = getDayName(element.day)
            tvOperationalDateTime?.text = generateDatetime(element.startTime, element.endTime)
            if (adapterPosition == operationalHoursList.lastIndex) {
                itemDivider?.invisible()
            }
        }

        private fun getDayName(day: Int): String {
            return dayNameMap[day] ?: ""
        }

        private fun formatDateTime(time: String): String {
            return time.replace(":", ".").substring(START_TIME_INDEX, END_TIME_INDEX)
        }

        private fun generateDatetime(startTime: String, endtime: String): String {
            return if (startTime == MIN_START_TIME && endtime == MAX_END_TIME) {
                // 24 hours
                context?.getString(R.string.shop_ops_hour_datetime_format_24_hours_text) ?: ""
            } else if (startTime == endtime) {
                context?.getString(R.string.shop_ops_hour_datetime_format_holiday_text) ?: ""
            } else {
                context?.getString(
                        R.string.shop_ops_hour_datetime_format_text,
                        formatDateTime(startTime),
                        formatDateTime(endtime)
                ) ?: ""
            }
        }
    }
}