package com.tokopedia.train.passenger.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TrainBookingAddPassengerContract {

    interface View extends CustomerView {

        int getPaxType();

        String getSalutationTitle();

        String getContactName();

        String getPhoneNumber();

        String getIdentityNumber();

        void showMessageErrorInSnackBar(int resId);

        void navigateToBookingPassenger(TrainPassengerViewModel trainPassengerViewModel);

        int getSpinnerPosition();

    }

    interface Presenter extends CustomerPresenter<View> {

        void submitDataPassenger(TrainPassengerViewModel trainPassengerViewModel);
    }
}
