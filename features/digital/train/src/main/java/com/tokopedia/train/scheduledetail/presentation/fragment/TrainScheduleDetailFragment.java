package com.tokopedia.train.scheduledetail.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainScheduleDetailFragment extends BaseDaggerFragment {

    private TextView trip;
    private TextView trainName;
    private TextView trainClass;
    private TextView date;

    private TextView departureTime;
    private TextView arrivalTime;
    private TextView departureDate;
    private TextView originStationName;
    private TextView originCityName;
    private TextView tripDuration;
    private TextView arrivalDate;
    private TextView destinationStationName;
    private TextView destinationCityName;

    public static Fragment createInstance() {
        return new TrainScheduleDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_schedule_detail, container, false);

        trip = rootview.findViewById(R.id.trip);
        trainName = rootview.findViewById(R.id.train_name);
        trainClass = rootview.findViewById(R.id.train_class);
        date = rootview.findViewById(R.id.date);
        departureTime = rootview.findViewById(R.id.departure_time);
        arrivalTime = rootview.findViewById(R.id.arrival_time);
        departureDate = rootview.findViewById(R.id.departure_date);
        originStationName = rootview.findViewById(R.id.origin_station_name);
        originCityName = rootview.findViewById(R.id.header_origin_city_name);
        tripDuration = rootview.findViewById(R.id.trip_duration);
        arrivalDate = rootview.findViewById(R.id.arrival_date);
        destinationStationName = rootview.findViewById(R.id.destination_station_name);
        destinationCityName = rootview.findViewById(R.id.header_destination_city_name);

        return rootview;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public void showScheduleDetail(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        trip.setText(trainScheduleDetailViewModel.isReturnTrip() ? "Perjalan Pulang" : "Perjalanan Pergi");
        trainName.setText(trainScheduleDetailViewModel.getTrainName());
        trainClass.setText(trainScheduleDetailViewModel.getTrainClass());
        date.setText(trainScheduleDetailViewModel.getDepartureDate());
        arrivalTime.setText(trainScheduleDetailViewModel.getArrivalTime());
        arrivalDate.setText(trainScheduleDetailViewModel.getArrivalDate());
        departureTime.setText(trainScheduleDetailViewModel.getDepartureTime());
        departureDate.setText(trainScheduleDetailViewModel.getDepartureDate());
        originStationName.setText(trainScheduleDetailViewModel.getOriginStationName());
        originCityName.setText(trainScheduleDetailViewModel.getOriginCityName());
        tripDuration.setText(trainScheduleDetailViewModel.getDuration());
        destinationStationName.setText(trainScheduleDetailViewModel.getDestinationStationName());
        destinationCityName.setText(trainScheduleDetailViewModel.getDestinationCityName());
    }

}
