package com.tokopedia.common.travel.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TravelPassengerUpdateContract {

    interface View extends CustomerView {

        int getPaxType();

        String getSalutationTitle();

        String getFirstName();

        String getLastName();

        String getIdentityNumber();

        void showMessageErrorInSnackBar(int resId);

        void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel);

        int getSpinnerPosition();

    }

    interface Presenter extends CustomerPresenter<View> {

        void submitDataPassenger(TravelPassenger trainPassengerViewModel);

        void onDestroyView();
    }
}
