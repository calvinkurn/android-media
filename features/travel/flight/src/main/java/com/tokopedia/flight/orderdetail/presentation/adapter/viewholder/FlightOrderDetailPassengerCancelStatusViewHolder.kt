package com.tokopedia.flight.orderdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationStatus
import com.tokopedia.flight.databinding.ItemFlightOrderDetailPassengerCancelStatusBinding
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerCancelStatusModel
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils

/**
 * @author by furqan on 30/10/2020
 */
class FlightOrderDetailPassengerCancelStatusViewHolder(val binding: ItemFlightOrderDetailPassengerCancelStatusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(passengerCancellationStatus: FlightOrderDetailPassengerCancelStatusModel) {
        with(binding) {
            tgFlightOrderPassengerCancelStatus.text = passengerCancellationStatus.statusStr

            when (passengerCancellationStatus.status) {
                0 -> false
                FlightCancellationStatus.REFUNDED -> {
                    tgFlightOrderPassengerCancelStatus.setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
                        )
                    )
                    OrderDetailUtils.changeShapeColor(
                        itemView.context,
                        tgFlightOrderPassengerCancelStatus.background,
                        com.tokopedia.unifyprinciples.R.color.Unify_G200
                    )
                }
                FlightCancellationStatus.ABORTED -> {
                    tgFlightOrderPassengerCancelStatus.setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_R500
                        )
                    )
                    OrderDetailUtils.changeShapeColor(
                        itemView.context,
                        tgFlightOrderPassengerCancelStatus.background,
                        com.tokopedia.unifyprinciples.R.color.Unify_R100
                    )
                }
                else -> {
                    tgFlightOrderPassengerCancelStatus.setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_Y500
                        )
                    )
                    OrderDetailUtils.changeShapeColor(
                        itemView.context,
                        tgFlightOrderPassengerCancelStatus.background,
                        com.tokopedia.unifyprinciples.R.color.Unify_Y200
                    )
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_order_detail_passenger_cancel_status
    }
}