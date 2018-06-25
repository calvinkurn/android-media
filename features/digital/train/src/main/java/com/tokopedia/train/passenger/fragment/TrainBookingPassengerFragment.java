package com.tokopedia.train.passenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.activity.TrainBookingPassengerActivity;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingPassengerFragment extends BaseDaggerFragment {

    private CardWithAction cardActionDeparture;
    private CardWithAction cardActionReturn;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;

    public static Fragment newInstance(TrainScheduleBookingPassData trainScheduleBookingPassData) {
        TrainBookingPassengerFragment fragment = new TrainBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainBookingPassengerActivity.TRAIN_SCHEDULE_BOOKING, trainScheduleBookingPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_booking_passenger, container, false);
        cardActionDeparture = view.findViewById(R.id.train_departure_info);
        cardActionReturn = view.findViewById(R.id.train_return_info);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeTripInfo();
    }

    private void initializeTripInfo() {
        String timeDepartureString;
        String timeArrivalString;
        trainScheduleBookingPassData = getArguments().getParcelable(TrainBookingPassengerActivity.TRAIN_SCHEDULE_BOOKING);

        TrainScheduleViewModel departureSchedule = trainScheduleBookingPassData.getDepartureTrip();
        cardActionDeparture.setVisibility(View.VISIBLE);
        cardActionDeparture.setContent(trainScheduleBookingPassData.getOriginCity() + " - " +
                trainScheduleBookingPassData.getDestinationCity());
        cardActionDeparture.setContentInfo(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, departureSchedule.getDepartureTimestamp()));
        cardActionDeparture.setSubContent(departureSchedule.getTrainName());
        timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, departureSchedule.getDepartureTimestamp());
        timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, departureSchedule.getArrivalTimestamp());
        cardActionDeparture.setSubContentInfo(" | " + timeDepartureString + " - " + timeArrivalString);

        if (trainScheduleBookingPassData.getReturnTrip() != null) {
            TrainScheduleViewModel returnSchedule = trainScheduleBookingPassData.getReturnTrip();
            cardActionReturn.setVisibility(View.VISIBLE);
            cardActionReturn.setContent(trainScheduleBookingPassData.getDestinationCity() + " - " +
                    trainScheduleBookingPassData.getOriginCity());
            cardActionReturn.setContentInfo(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.DEFAULT_VIEW_FORMAT, returnSchedule.getDepartureTimestamp()));
            cardActionReturn.setSubContent(returnSchedule.getTrainName());
            timeDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.FORMAT_TIME, returnSchedule.getDepartureTimestamp());
            timeArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.FORMAT_TIME, returnSchedule.getArrivalTimestamp());
            cardActionReturn.setSubContentInfo(" | " + timeDepartureString + " - " + timeArrivalString);
        } else {
            cardActionReturn.setVisibility(View.GONE);
        }

        cardActionDeparture.setActionListener(new CardWithAction.ActionListener() {
            @Override
            public void actionClicked() {
                //TODO : detail info trip departure
                Toast.makeText(getActivity(), trainScheduleBookingPassData.getOriginCity() + " - " +
                        trainScheduleBookingPassData.getDestinationCity(), Toast.LENGTH_SHORT).show();
            }
        });

        cardActionReturn.setActionListener(new CardWithAction.ActionListener() {
            @Override
            public void actionClicked() {
                //TODO : detail info trip return
                Toast.makeText(getActivity(), trainScheduleBookingPassData.getDestinationCity() + " - " +
                        trainScheduleBookingPassData.getOriginCity(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
