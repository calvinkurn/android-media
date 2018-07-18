package com.tokopedia.train.reviewdetail.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailContract {

    public interface View extends BaseListViewListener<TrainReviewPassengerInfoViewModel> {

        void showDepartureTrip(TrainScheduleViewModel departureTrip);

        void showReturnTrip(TrainScheduleViewModel returnTrip);

        void showScheduleTripsPrice(TrainScheduleDetailViewModel first, TrainScheduleDetailViewModel second);

        void hideReturnTrip();

    }

    public interface Presenter {

        void getPassengers(TrainSoftbook trainSoftbook, String origin, String destination);

        void getScheduleTrips(String departureScheduleId, String returnScheduleId);

        void getScheduleTripsPrice(String departureScheduleId, String returnScheduleId, int adultPassenger, int infantPassenger);

    }

}
