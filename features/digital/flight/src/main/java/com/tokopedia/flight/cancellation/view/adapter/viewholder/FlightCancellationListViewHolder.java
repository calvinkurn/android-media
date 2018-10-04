package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.constant.FlightCancellationStatus;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListViewHolder extends AbstractViewHolder<FlightCancellationListViewModel> {

    public static final int LAYOUT = R.layout.item_flight_cancellation_list;

    TextViewCompat txtCancellationId;
    TextViewCompat txtJourney;
    TextViewCompat txtCreatedTime;
    TextViewCompat txtCancellationStatus;
    Context context;

    public FlightCancellationListViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        txtCancellationId = itemView.findViewById(R.id.txt_cancellation_id);
        txtJourney = itemView.findViewById(R.id.txt_cancellation_journey);
        txtCreatedTime = itemView.findViewById(R.id.txt_cancellation_created_time);
        txtCancellationStatus = itemView.findViewById(R.id.txt_cancellation_status);
    }

    @Override
    public void bind(FlightCancellationListViewModel element) {
        txtCancellationId.setText(String.format(getString(R.string.flight_cancellation_list_id),
                element.getCancellations().getRefundId()));
        txtCreatedTime.setText(String.format(getString(R.string.flight_cancellation_list_created_time),
                element.getCancellations().getCreateTime()));
        if (element.getCancellations().getJourneys().size() > 0 &&
                element.getCancellations().getJourneys().get(0) != null) {
            txtJourney.setText(String.format(getString(R.string.flight_label_detail_format),
                    element.getCancellations().getJourneys().get(0).getDepartureCity(),
                    element.getCancellations().getJourneys().get(0).getDepartureAiportId(),
                    element.getCancellations().getJourneys().get(0).getArrivalCity(),
                    element.getCancellations().getJourneys().get(0).getArrivalAirportId()));
        }
        checkCancellationStatus(element.getCancellations().getStatus());
    }

    private void checkCancellationStatus(int status) {
        switch (status) {
            case FlightCancellationStatus.REQUESTED:
                txtCancellationStatus.setText(R.string.flight_cancellation_list_status_requested);
                txtCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_process));
                break;
            case FlightCancellationStatus.PENDING:
                txtCancellationStatus.setText(R.string.flight_cancellation_list_status_pending);
                txtCancellationStatus.setTextAppearance(context, R.style.CardProcessStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_process));
                break;
            case FlightCancellationStatus.REFUNDED:
                txtCancellationStatus.setText(R.string.flight_cancellation_list_status_refunded);
                txtCancellationStatus.setTextAppearance(context, R.style.CardSuccessStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_success));
                break;
            case FlightCancellationStatus.ABORTED:
                txtCancellationStatus.setText(R.string.flight_cancellation_list_status_aborted);
                txtCancellationStatus.setTextAppearance(context, R.style.CardFailedStatusStyle);
                txtCancellationStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_card_failed));
                break;
        }
    }

}
