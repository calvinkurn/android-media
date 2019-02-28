package com.tokopedia.common.travel.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public interface TravelPassengerEditContract {

    interface View extends CustomerView {
        void showMessageErrorInSnackBar(int resId);

        void showMessageErrorInSnackBar(String message);

        void showMessageErrorInSnackBar(Throwable throwable);

        void showSuccessSnackbar(String message);

        void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel);

        void renderPassengerList(List<TravelPassenger> travelPassengerList);

        void showProgressBar();

        void hideProgressBar();

        int getTravelPlatformType();

        void successDeletePassenger();

        TravelPassenger getTravelPassengerBooking();

    }

    interface Presenter extends CustomerPresenter<View> {
        void getPassengerList();

        void deletePassenger(String idPassenger, String id, int travelId);

        void onDestroyView();
    }
}
