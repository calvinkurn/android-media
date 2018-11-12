package com.tokopedia.common.travel.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TravelPassengerBookingListContract {

    interface View extends CustomerView {

        void showMessageErrorInSnackBar(int resId);

        void showMessageErrorInSnackBar(String message);

        void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel);

        void renderPassengerList(List<TravelPassenger> travelPassengerList);

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getPassengerList(boolean resetPassengerListSelected);

        void updatePassenger(String travelIdPassenger, boolean isSelected);

        void onDestroyView();
    }
}
