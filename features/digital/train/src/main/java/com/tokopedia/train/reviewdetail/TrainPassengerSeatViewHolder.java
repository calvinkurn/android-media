package com.tokopedia.train.reviewdetail;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainPassengerSeatViewHolder extends AbstractViewHolder<TrainReviewPassengerInfoViewModel> {

    public static final int LAYOUT = R.layout.item_train_passenger_info;

    private TextView textPassengerIndex;
    private TextView textPassengerName;
    private TextView textNumberID;
    private TextView textDepartureTrip;
    private TextView textDepartureSeat;
    private TextView textReturnSeat;

    private String originStation;
    private String destinationStation;

    public TrainPassengerSeatViewHolder(View itemView, String originCity, String destinationCity) {
        super(itemView);
        textPassengerIndex = itemView.findViewById(R.id.text_passenger_index);
        textPassengerName = itemView.findViewById(R.id.text_passenger_name);
        textNumberID = itemView.findViewById(R.id.text_number_id);
        textDepartureSeat = itemView.findViewById(R.id.text_departure_seat);
        textDepartureTrip = itemView.findViewById(R.id.text_departure_trip);
        textReturnSeat = itemView.findViewById(R.id.text_return_seat);
        this.originStation = originCity;
        this.destinationStation = destinationCity;
    }

    @Override
    public void bind(TrainReviewPassengerInfoViewModel element) {
        textPassengerName.setText(element.getName());
        textNumberID.setText(element.getNoID());
        textDepartureSeat.setText(element.getDepartureTripClass() + " " + element.getDepartureSeat());
        textDepartureTrip.setText("Kursi (" + originStation + " - " +
                destinationStation + ")");
    }

}