package com.tokopedia.train.scheduledetail.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private static final String TRAIN_SCHEDULE_DETAIL = "train_schedule_detail";

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

    private TrainScheduleDetailViewModel viewModel;

    public static Fragment createInstance(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        Fragment fragment = new TrainScheduleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TRAIN_SCHEDULE_DETAIL, trainScheduleDetailViewModel);
        fragment.setArguments(bundle);
        return fragment;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getArguments().getParcelable(TRAIN_SCHEDULE_DETAIL);
        if (viewModel != null)
            render(viewModel);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public void showScheduleDetail(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        viewModel = getArguments().getParcelable("TRAIN_DETAIL");
        if (isAdded()) {
            render(viewModel);
        }
    }

    private void render(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        trip.setText(trainScheduleDetailViewModel.isReturnTrip() ?
                R.string.train_search_return_title :
                R.string.train_search_departure_title);
        trainName.setText(trainScheduleDetailViewModel.getTrainName() + " " +
                trainScheduleDetailViewModel.getTrainNumber());
        trainClass.setText(trainScheduleDetailViewModel.getTrainClass() + " (" +
                trainScheduleDetailViewModel.getSubclass() + ")");
        date.setText(trainScheduleDetailViewModel.getDepartureDate());
        arrivalTime.setText(trainScheduleDetailViewModel.getArrivalTime());
        arrivalDate.setText(trainScheduleDetailViewModel.getArrivalDate());
        departureTime.setText(trainScheduleDetailViewModel.getDepartureTime());
        departureDate.setText(trainScheduleDetailViewModel.getDepartureDate());
        originStationName.setText(trainScheduleDetailViewModel.getOriginStationName() + " ("
                + trainScheduleDetailViewModel.getOriginStationCode() + ")");
        originCityName.setText(trainScheduleDetailViewModel.getOriginCityName());
        tripDuration.setText(trainScheduleDetailViewModel.getDuration());
        destinationStationName.setText(trainScheduleDetailViewModel.getDestinationStationName() + " ("
                + trainScheduleDetailViewModel.getDestinationStationCode() + ")");
        destinationCityName.setText(trainScheduleDetailViewModel.getDestinationCityName());
    }

}
