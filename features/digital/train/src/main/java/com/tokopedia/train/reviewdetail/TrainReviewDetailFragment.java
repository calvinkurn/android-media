package com.tokopedia.train.reviewdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.scheduledetail.presentation.activity.TrainScheduleDetailActivity;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailFragment extends BaseListFragment<TrainReviewPassengerInfoViewModel, TrainPassengerAdapterTypeFactory>
        implements TrainReviewDetailContract.View {

    @Inject
    TrainReviewDetailPresenter trainReviewDetailPresenter;

    private CardWithAction cardDepartureTrip;
    private CardWithAction cardReturnTrip;

    private static final String ARGS_TRAIN_SOFTBOOK = "ARGS_TRAIN_SOFTBOOK";
    private static final String ARGS_TRAIN_SCHEDULE_BOOKING = "ARGS_TRAIN_SCHEDULE_BOOKING";

    private TrainSoftbook trainSoftbook;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;

    private TrainReviewDetailComponent trainReviewDetailComponent;

    public static Fragment newInstance(TrainSoftbook trainSoftbook,
                                       TrainScheduleBookingPassData trainScheduleBookingPassData) {
        TrainReviewDetailFragment fragment = new TrainReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_TRAIN_SOFTBOOK, trainSoftbook);
        bundle.putParcelable(ARGS_TRAIN_SCHEDULE_BOOKING, trainScheduleBookingPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        trainSoftbook = getArguments().getParcelable(ARGS_TRAIN_SOFTBOOK);
        trainScheduleBookingPassData = getArguments().getParcelable(ARGS_TRAIN_SCHEDULE_BOOKING);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainReviewDetailPresenter.attachView(this);

        trainReviewDetailPresenter.getScheduleDetail(trainScheduleBookingPassData.getDepartureScheduleId(),
                trainScheduleBookingPassData.getReturnScheduleId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_review_detail, container, false);

        cardDepartureTrip = rootview.findViewById(R.id.train_departure_info);
        cardReturnTrip = rootview.findViewById(R.id.train_return_info);

        cardDepartureTrip.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getDepartureScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
        });

        cardReturnTrip.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getReturnScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
        });

        return rootview;
    }

    @Override
    public void loadData(int page) {

    }

    @NonNull
    @Override
    protected BaseListAdapter<TrainReviewPassengerInfoViewModel, TrainPassengerAdapterTypeFactory> createAdapterInstance() {
        return super.createAdapterInstance();
    }

    @Override
    protected TrainPassengerAdapterTypeFactory getAdapterTypeFactory() {
        return new TrainPassengerAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(TrainReviewPassengerInfoViewModel trainSeatPassengerViewModel) {

    }

    @Override
    protected void initInjector() {
        if (trainReviewDetailComponent == null) {
            trainReviewDetailComponent = DaggerTrainReviewDetailComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getActivity().getApplication())).build();
        }
        trainReviewDetailComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showScheduleTrips(TrainScheduleViewModel departureTrip, TrainScheduleViewModel returnTrip) {
        cardDepartureTrip.setContent(departureTrip.getOrigin() + " - " + departureTrip.getDestination());
        cardDepartureTrip.setContentInfo(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.DEFAULT_VIEW_FORMAT, departureTrip.getDepartureTimestamp()));
        cardDepartureTrip.setSubContent(departureTrip.getTrainName());
        String timeDepartureDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, departureTrip.getDepartureTimestamp());
        String timeDepartureArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, departureTrip.getArrivalTimestamp());
        cardDepartureTrip.setSubContentInfo(" | " + timeDepartureDepartureString + " - " + timeDepartureArrivalString);

        if (returnTrip != null) {
            cardReturnTrip.setContent(returnTrip.getOrigin() + " - " + returnTrip.getDestination());
            cardReturnTrip.setContentInfo(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.DEFAULT_VIEW_FORMAT, returnTrip.getDepartureTimestamp()));
            cardReturnTrip.setSubContent(returnTrip.getTrainName());
            String timeReturnDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.FORMAT_TIME, returnTrip.getDepartureTimestamp());
            String timeReturnArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.FORMAT_TIME, returnTrip.getArrivalTimestamp());
            cardReturnTrip.setSubContentInfo(" | " + timeReturnDepartureString + " - " + timeReturnArrivalString);
        } else {
            cardReturnTrip.setVisibility(View.GONE);
        }

        trainReviewDetailPresenter.getPassengers(trainSoftbook, departureTrip.getOrigin(),
                departureTrip.getDestination());
    }

}