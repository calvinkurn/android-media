package com.tokopedia.train.passenger.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;

import java.util.List;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TrainBookingPassengerContract {

    interface View extends CustomerView {

        String getString(@StringRes int resId);

        void renderPassenger(List<TrainPassengerViewModel> trainPassengerViewModels);

        void setCurrentListPassenger(List<TrainPassengerViewModel> trainPassengerViewModels);

        List<TrainPassengerViewModel> getCurrentPassengerList();
    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitPassengers(int adultPassengers, int infantPassengers);

        void updateDataPassengers(TrainPassengerViewModel trainPassengerViewModel);
    }
}
