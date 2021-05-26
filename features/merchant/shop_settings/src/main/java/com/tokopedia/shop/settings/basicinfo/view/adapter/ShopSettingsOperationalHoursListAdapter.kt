package com.tokopedia.shop.settings.basicinfo.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.unifyprinciples.Typography

class ShopSettingsOperationalHoursListAdapter(
        private val opsHourList: MutableList<ShopOperationalHour> = mutableListOf()
) : RecyclerView.Adapter<ShopSettingsOperationalHoursListAdapter.ViewHolder>() {

    companion object {
        @LayoutRes
        val VIEWHOLDER_LAYOUT = R.layout.item_shop_operational_hours_list
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
        opsHourList.apply {
            clear()
            addAll(newList)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvOpsHourDay: Typography? = null
        private var tvOpsHourTime: Typography? = null

        init {
            tvOpsHourDay = itemView.findViewById(R.id.ops_hour_day)
            tvOpsHourTime = itemView.findViewById(R.id.ops_hour_time)
        }

        fun bind(item: ShopOperationalHour) {
            tvOpsHourDay?.text = OperationalHoursUtil.getDayName(item.day)
            tvOpsHourTime?.text = OperationalHoursUtil.generateDatetime(item.startTime, item.endTime).substring(4)
        }
    }

}