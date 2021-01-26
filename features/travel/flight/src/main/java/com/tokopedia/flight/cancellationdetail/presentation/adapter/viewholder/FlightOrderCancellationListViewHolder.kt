package com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationStatusEnum
import kotlinx.android.synthetic.main.item_flight_cancellation_list.view.*

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationListViewHolder(view: View)
    : AbstractViewHolder<FlightOrderCancellationListModel>(view) {

    override fun bind(element: FlightOrderCancellationListModel) {
        with(itemView) {
            tgCancellationCreatedTime.text = String.format(getString(
                    R.string.flight_cancellation_list_created_time),
                    element.cancellationDetail.createTime)

            if (element.cancellationDetail.journeys.isNotEmpty()) {
                element.cancellationDetail.journeys[0].let {
                    tgCancellationJourney.text = String.format(getString(
                            R.string.flight_label_detail_format),
                            it.departureCityName,
                            it.departureId,
                            it.arrivalCityName,
                            it.arrivalId)
                }
            }

            setupCancellationStatus(element.cancellationDetail.status, element.cancellationDetail.statusStr)
            tgCancellationNotes.text = element.cancellationDetail.refundInfo
        }
    }

    private fun setupCancellationStatus(status: Int, statusStr: String) {
        with(itemView) {
            tgCancellationStatus.text = statusStr
            when (status) {
                0 -> {
                }
                FlightOrderCancellationStatusEnum.REQUESTED.id -> {
                    tgCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle)
                    tgCancellationStatus.background = context.resources.getDrawable(R.drawable.flight_bg_card_process)

                }
                FlightOrderCancellationStatusEnum.REFUNDED.id -> {
                    tgCancellationStatus.setTextAppearance(context, R.style.CardSuccessStatusStyle)
                    tgCancellationStatus.background = context.resources.getDrawable(R.drawable.flight_bg_card_success)
                }
                FlightOrderCancellationStatusEnum.ABORTED.id -> {
                    tgCancellationStatus.setTextAppearance(context, R.style.CardFailedStatusStyle)
                    tgCancellationStatus.background = context.resources.getDrawable(R.drawable.flight_bg_card_failed)
                }
                else -> {
                    tgCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle)
                    tgCancellationStatus.background = context.resources.getDrawable(R.drawable.flight_bg_card_process)
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_list
    }
}