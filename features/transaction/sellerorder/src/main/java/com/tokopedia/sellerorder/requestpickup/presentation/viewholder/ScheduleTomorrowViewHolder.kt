package com.tokopedia.sellerorder.requestpickup.presentation.viewholder

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.requestpickup.data.model.SchedulePickupModelVisitable
import com.tokopedia.sellerorder.requestpickup.data.model.Today
import com.tokopedia.sellerorder.requestpickup.data.model.Tomorrow
import com.tokopedia.sellerorder.requestpickup.presentation.adapter.SomConfirmSchedulePickupAdapter
import com.tokopedia.unifyprinciples.Typography

class ScheduleTomorrowViewHolder(itemView: View) : SomConfirmSchedulePickupAdapter.BaseViewHolder<SchedulePickupModelVisitable>(itemView) {

    private val timeTomorrow = itemView.findViewById<Typography>(R.id.time_tomorrow)

    override fun bind(item: SchedulePickupModelVisitable, position: Int) {
        if (item is Tomorrow) {
            val time = item.startTomorrow + "-" + item.endTomorrow
            timeTomorrow.text = time
        }
    }

}