package com.tokopedia.train.reviewdetail.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.Date;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailContract {

    public interface View extends BaseListViewListener<TrainReviewPassengerInfoViewModel> {

        void showDepartureTrip(TrainScheduleViewModel departureTrip);

        void showReturnTrip(TrainScheduleViewModel returnTrip);

        void showScheduleTripsPrice(TrainScheduleDetailViewModel first, TrainScheduleDetailViewModel second);

        void hideReturnTrip();

        void startCountdown(Date expiredDate);

        void navigateToTopPayActivity(TrainCheckoutViewModel trainCheckoutViewModel);

        void showPaymentFailedErrorMessage(int resId);

        void setNeedToRefreshOnPassengerInfo();

        void navigateToOrderList();

        void showCheckoutLoading();

        String getExpireDate();

        void showExpiredPaymentDialog();

        void showCheckoutFailed(String message);

        String getPassengerTypeAdult();

        String getPassengerTypeChild();

    }

    public interface Presenter {

        void getPassengers(TrainSoftbook trainSoftbook, String origin, String destination);

        void getScheduleTrips(String departureScheduleId, String returnScheduleId);

        void getScheduleTripsPrice(String departureScheduleId, String returnScheduleId, int adultPassenger, int infantPassenger);

        void checkout(String reservationId, String tokpedBookCode, String galaCode, String client, String version);

        void onPaymentSuccess();

        void onPaymentFailed();

        void onPaymentCancelled();

        void onRunningOutOfTime();

    }

}
