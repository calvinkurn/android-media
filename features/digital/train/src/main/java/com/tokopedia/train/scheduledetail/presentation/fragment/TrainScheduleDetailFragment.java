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
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainScheduleDetailFragment extends BaseDaggerFragment {

    public static final String EXTRA_TRAIN_SCHEDULE_VIEW_MODEL = "EXTRA_TRAIN_SCHEDULE_VIEW_MODEL";

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

    private TrainScheduleDetailViewModel trainScheduleDetailViewModel;

    public static Fragment createInstance(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        TrainScheduleDetailFragment fragment = new TrainScheduleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_TRAIN_SCHEDULE_VIEW_MODEL, trainScheduleDetailViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trainScheduleDetailViewModel = getArguments().getParcelable(EXTRA_TRAIN_SCHEDULE_VIEW_MODEL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_schedule_detail, container, false);

        trainName = rootview.findViewById(R.id.train_name);
        trainClass = rootview.findViewById(R.id.train_class);
        date = rootview.findViewById(R.id.date);
        departureTime = rootview.findViewById(R.id.departure_time);
        arrivalTime = rootview.findViewById(R.id.arrival_time);
        departureDate = rootview.findViewById(R.id.departure_date);
        originStationName = rootview.findViewById(R.id.origin_station_name);
        originCityName = rootview.findViewById(R.id.origin_city_name);
        tripDuration = rootview.findViewById(R.id.trip_duration);
        arrivalDate = rootview.findViewById(R.id.arrival_date);
        destinationStationName = rootview.findViewById(R.id.destination_station_name);
        destinationCityName = rootview.findViewById(R.id.destination_city_name);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainName.setText(trainScheduleDetailViewModel.getTrainName());
        trainClass.setText(trainScheduleDetailViewModel.getTrainClass());
        date.setText(trainScheduleDetailViewModel.getDepartureDate());
        departureTime.setText(trainScheduleDetailViewModel.getDepartureTime());
        arrivalTime.setText(trainScheduleDetailViewModel.getArrivalTime());
        departureDate.setText(trainScheduleDetailViewModel.getDepartureDate());
        originStationName.setText(trainScheduleDetailViewModel.getOriginStationName());
        originCityName.setText(trainScheduleDetailViewModel.getOriginCityName());
        tripDuration.setText(trainScheduleDetailViewModel.getDuration());
        arrivalDate.setText(trainScheduleDetailViewModel.getArrivalDate());
        destinationStationName.setText(trainScheduleDetailViewModel.getDestinationStationName());
        destinationCityName.setText(trainScheduleDetailViewModel.getDestinationCityName());
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

}
