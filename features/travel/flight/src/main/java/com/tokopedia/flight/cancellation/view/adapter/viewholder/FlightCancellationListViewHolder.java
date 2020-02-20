package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.orderlist.constant.FlightCancellationStatus;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListViewHolder extends AbstractViewHolder<FlightCancellationListViewModel> {

    public static final int LAYOUT = com.tokopedia.flight.R.layout.item_flight_cancellation_list;

    Typography txtJourney;
    Typography txtCreatedTime;
    Typography txtCancellationStatus;
    Typography txtCancellationNotes;
    Context context;

    public FlightCancellationListViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        txtJourney = itemView.findViewById(com.tokopedia.flight.R.id.txt_cancellation_journey);
        txtCreatedTime = itemView.findViewById(com.tokopedia.flight.R.id.txt_cancellation_created_time);
        txtCancellationStatus = itemView.findViewById(com.tokopedia.flight.R.id.txt_cancellation_status);
        txtCancellationNotes = itemView.findViewById(com.tokopedia.flight.R.id.txt_cancellation_notes);
    }

    @Override
    public void bind(FlightCancellationListViewModel element) {
        txtCreatedTime.setText(String.format(getString(com.tokopedia.flight.R.string.flight_cancellation_list_created_time),
                element.getCancellations().getCreateTime()));
        if (element.getCancellations().getJourneys().size() > 0 &&
                element.getCancellations().getJourneys().get(0) != null) {
            txtJourney.setText(String.format(getString(com.tokopedia.flight.R.string.flight_label_detail_format),
                    element.getCancellations().getJourneys().get(0).getDepartureCity(),
                    element.getCancellations().getJourneys().get(0).getDepartureAiportId(),
                    element.getCancellations().getJourneys().get(0).getArrivalCity(),
                    element.getCancellations().getJourneys().get(0).getArrivalAirportId()));
        }
        checkCancellationStatus(element.getCancellations().getStatus(), element.getCancellations().getStatusStr());
        txtCancellationNotes.setText(element.getCancellations().getRefundInfo());
    }

    private void checkCancellationStatus(int status, String statusStr) {
        txtCancellationStatus.setText(statusStr);
        switch (status) {
            case 0 : break;
            case FlightCancellationStatus.REQUESTED:
                txtCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(com.tokopedia.flight.R.drawable.flight_bg_card_process));
                break;
            case FlightCancellationStatus.REFUNDED:
                txtCancellationStatus.setTextAppearance(context, R.style.CardSuccessStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(com.tokopedia.flight.R.drawable.flight_bg_card_success));
                break;
            case FlightCancellationStatus.ABORTED:
                txtCancellationStatus.setTextAppearance(context, R.style.CardFailedStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(com.tokopedia.flight.R.drawable.flight_bg_card_failed));
                break;
            default:
                txtCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(com.tokopedia.flight.R.drawable.flight_bg_card_process));
        }
    }

}
