package com.tokopedia.common.travel.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TravelPassengerBookingContract {

    interface View extends CustomerView {

        int getPaxType();

        String getSalutationTitle();

        String getContactName();

        String getPhoneNumber();

        String getIdentityNumber();

        void showMessageErrorInSnackBar(int resId);

        void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel);

        int getSpinnerPosition();

        void renderPassengerList(List<TravelPassenger> travelPassengerList);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getPassengerList();

        void submitDataPassenger(TravelPassenger trainPassengerViewModel);

        void onDestroyView();
    }
}
