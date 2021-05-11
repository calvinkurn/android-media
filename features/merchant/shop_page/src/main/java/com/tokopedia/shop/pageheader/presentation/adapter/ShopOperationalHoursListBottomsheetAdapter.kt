package com.tokopedia.shop.pageheader.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.util.OperationalHoursUtil
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

        private var tvOperationalDay: Typography? = null
        private var tvOperationalDateTime: Typography? = null
        private var itemDivider: View? = null

        init {
            tvOperationalDay = itemView.findViewById(R.id.tvOperationalDay)
            tvOperationalDateTime = itemView.findViewById(R.id.tvOperationalDateTime)
            itemDivider = itemView.findViewById(R.id.item_separator_ops_hour)
        }

        fun bind(element: ShopOperationalHour) {
            tvOperationalDay?.text = OperationalHoursUtil.getDayName(element.day)
            tvOperationalDateTime?.text = OperationalHoursUtil.generateDatetime(element.startTime, element.endTime)
            if (adapterPosition == operationalHoursList.lastIndex) {
                itemDivider?.invisible()
            }
        }

    }
}