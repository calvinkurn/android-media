package com.tokopedia.shop.settings.basicinfo.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.common.util.OperationalHoursUtil.ALL_DAY
import com.tokopedia.shop.common.util.OperationalHoursUtil.CANNOT_ATC_TEXT
import com.tokopedia.shop.common.util.OperationalHoursUtil.CAN_ATC_STATUS
import com.tokopedia.shop.common.util.OperationalHoursUtil.CAN_ATC_TEXT
import com.tokopedia.shop.common.util.OperationalHoursUtil.HOLIDAY
import com.tokopedia.shop.common.util.OperationalHoursUtil.HOLIDAY_CANNOT_ATC
import com.tokopedia.shop.common.util.OperationalHoursUtil.HOLIDAY_CAN_ATC
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.model.ShopOperationalHourUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopSettingsOperationalHoursListAdapter(
        private val opsHourList: MutableList<ShopOperationalHourUiModel> = mutableListOf()
) : RecyclerView.Adapter<ShopSettingsOperationalHoursListAdapter.ViewHolder>() {

    companion object {
        @LayoutRes
        val VIEWHOLDER_LAYOUT = R.layout.item_shop_operational_hours_list

        private const val INITIAL_SIZE = 0
        private const val DASH_DELIMITER = "-"
        private const val AFTER_DASH_DELIMITER = "- "
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, VIEWHOLDER_LAYOUT, null))
    }

    override fun getItemCount(): Int {
        return opsHourList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(opsHourList[position])
    }

    fun updateOpsHourList(newList: List<ShopOperationalHour>) {
        val opsHourListUiModel = if (newList.isEmpty()) {
            generateOpsHourUiModel(OperationalHoursUtil.generateDefaultOpsHourList())
        } else {
            generateOpsHourUiModel(newList)
        }
        opsHourList.apply {
            clear()
            addAll(opsHourListUiModel)
            notifyDataSetChanged()
        }
    }

    private fun generateOpsHourUiModel(opsHourList: List<ShopOperationalHour>): List<ShopOperationalHourUiModel> {
        val opsHourListUiModel = mutableListOf<ShopOperationalHourUiModel>()
        val groupedOpsHourListUiModel = mutableListOf<ShopOperationalHourUiModel>()
        var total = INITIAL_SIZE

        opsHourList.forEach { opsHour ->
            val currentDayName = OperationalHoursUtil.getDayName(opsHour.day)
            val operationalHours = OperationalHoursUtil.generateDatetime(opsHour.startTime, opsHour.endTime, opsHour.status)
            val operationalHoursDescription = if (opsHour.status == CAN_ATC_STATUS) {
                CAN_ATC_TEXT
            } else {
                CANNOT_ATC_TEXT
            }
            val operationalHoursTextFormatted = when (operationalHours) {
                HOLIDAY_CAN_ATC, HOLIDAY_CANNOT_ATC -> HOLIDAY
                else -> operationalHours
            }

            opsHourListUiModel.add(ShopOperationalHourUiModel(
                    dayName = currentDayName,
                    shopOperationalHours = operationalHoursTextFormatted,
                    shopOperationalHoursDescription = operationalHoursDescription
            ))
        }

        groupedOpsHourListUiModel.add(opsHourListUiModel[total])
        total++

        // grouping operational hours schedule
        opsHourListUiModel.forEachIndexed { idx, shopOperationalHourUiModel ->
            if (idx > INITIAL_SIZE) {
                val groupedOpsTime = "${groupedOpsHourListUiModel[total-1].shopOperationalHours} - ${groupedOpsHourListUiModel[total-1].shopOperationalHoursDescription}"
                val currentOpsTime = "${shopOperationalHourUiModel.shopOperationalHours} - ${shopOperationalHourUiModel.shopOperationalHoursDescription}"

                if (groupedOpsTime == currentOpsTime) {
                    val currentDayName = shopOperationalHourUiModel.dayName
                    var existingDayName = groupedOpsHourListUiModel[total-1].dayName
                    existingDayName = if (existingDayName.contains(DASH_DELIMITER)) {
                        existingDayName.replaceAfter(AFTER_DASH_DELIMITER, currentDayName)
                    }
                    else {
                        "$existingDayName - $currentDayName"
                    }
                    groupedOpsHourListUiModel[total-1].dayName = existingDayName
                } else {
                    groupedOpsHourListUiModel.add(shopOperationalHourUiModel)
                    total++
                }
            }
        }

        return groupedOpsHourListUiModel
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvOpsHourDay: Typography? = null
        private var tvOpsHourTime: Typography? = null
        private var tvOpsHourTimeDescription: Typography? = null

        init {
            tvOpsHourDay = itemView.findViewById(R.id.ops_hour_day)
            tvOpsHourTime = itemView.findViewById(R.id.ops_hour_time)
            tvOpsHourTimeDescription = itemView.findViewById(R.id.ops_hour_time_description)
        }

        fun bind(item: ShopOperationalHourUiModel) {
            tvOpsHourDay?.text = item.dayName
            tvOpsHourTime?.text = item.shopOperationalHours
            tvOpsHourTimeDescription?.shouldShowWithAction(isShowTimeDescription(item.shopOperationalHours)) {
                tvOpsHourTimeDescription?.text = item.shopOperationalHoursDescription
            }
        }

        private fun isShowTimeDescription(hoursText: String): Boolean {
            return hoursText == HOLIDAY
        }
    }

}