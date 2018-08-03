package com.tokopedia.train.reviewdetail.presentation.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainPassengerSeatViewHolder extends AbstractViewHolder<TrainReviewPassengerInfoViewModel> {

    public static final int LAYOUT = R.layout.item_train_passenger_info;

    private TextView textPassengerIndex;
    private TextView textPassengerName;
    private TextView textNumberID;
    private TextView textDepartureTrip;
    private TextView textReturnTrip;
    private TextView textDepartureSeat;
    private TextView textReturnSeat;

    private LinearLayout layoutTextNoId;
    private LinearLayout layoutTextDepartureTrip;
    private LinearLayout layoutTextReturnTrip;

    public TrainPassengerSeatViewHolder(View itemView) {
        super(itemView);
        textPassengerIndex = itemView.findViewById(R.id.text_passenger_index);
        textPassengerName = itemView.findViewById(R.id.text_passenger_name);
        textNumberID = itemView.findViewById(R.id.text_number_id);
        textDepartureSeat = itemView.findViewById(R.id.text_departure_seat);
        textDepartureTrip = itemView.findViewById(R.id.text_departure_trip);
        textReturnTrip = itemView.findViewById(R.id.text_returnure_trip);
        textReturnSeat = itemView.findViewById(R.id.text_return_seat);
        layoutTextNoId = itemView.findViewById(R.id.layout_text_no_id);
        layoutTextDepartureTrip = itemView.findViewById(R.id.layout_text_departure_trip);
        layoutTextReturnTrip = itemView.findViewById(R.id.layout_text_return_trip);
    }

    @Override
    public void bind(TrainReviewPassengerInfoViewModel element) {
        if (element.getPassengerType() == TrainBookingPassenger.ADULT) {
            layoutTextNoId.setVisibility(View.VISIBLE);
            layoutTextDepartureTrip.setVisibility(View.VISIBLE);
            textPassengerIndex.setText(itemView.getContext()
                    .getString(R.string.train_review_item_passenger_index, getAdapterPosition() + 1));
            textPassengerName.setText(itemView.getContext().getString(R.string.train_review_item_passenger_name,
                    element.getName(), element.getPassengerTypeStr()));
            textNumberID.setText(element.getNoID());
            textDepartureSeat.setText(itemView.getContext().getString(R.string.train_review_item_passenger_seat,
                    element.getDepartureSeat()));
            textDepartureTrip.setText(itemView.getContext().getString(R.string.train_review_item_passenger_info_seat,
                    element.getOriginStationCode(), element.getDestinationStationCode()));
            if (!TextUtils.isEmpty(element.getReturnSeat())) {
                layoutTextReturnTrip.setVisibility(View.VISIBLE);
                textReturnSeat.setText(itemView.getContext().getString(R.string.train_review_item_passenger_seat,
                        element.getReturnSeat()));
                textReturnTrip.setText(itemView.getContext().getString(R.string.train_review_item_passenger_info_seat,
                        element.getDestinationStationCode(), element.getOriginStationCode()));
            } else {
                layoutTextReturnTrip.setVisibility(View.GONE);
            }
        } else {
            textPassengerIndex.setText(itemView.getContext()
                    .getString(R.string.train_review_item_passenger_index, getAdapterPosition() + 1));
            textPassengerName.setText(itemView.getContext().getString(R.string.train_review_item_passenger_name,
                    element.getName(), element.getPassengerTypeStr()));
            layoutTextNoId.setVisibility(View.GONE);
            layoutTextDepartureTrip.setVisibility(View.GONE);
            layoutTextReturnTrip.setVisibility(View.GONE);
        }
    }

}