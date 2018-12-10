package com.tokopedia.common.travel.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TravelPassengerListContract {

    interface View extends CustomerView {

        void showMessageErrorInSnackBar(int resId);

        void showActionErrorInSnackBar(TravelPassenger travelPassenger, int resId);

        void showMessageErrorInSnackBar(Throwable throwable);

        void renderPassengerList(List<TravelPassenger> travelPassengerList);

        void updatePassengerSelected(TravelPassenger travelPassengerSelected);

        void showProgressBar();

        void hideProgressBar();

        void onClickSelectPassenger(TravelPassenger travelPassenger);

        void successUpdatePassengerDb();

        void failedUpdatePassengerDb();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getPassengerList(boolean resetPassengerListSelected, int idLocal, String idPassengerSelected);

        void selectPassenger(TravelPassenger passengerBooking, TravelPassenger travelPassenger);

        void updatePassenger(String travelIdPassenger, boolean isSelected);

        void onDestroyView();
    }
}
