package com.tokopedia.train.passenger.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

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

        void loadDetailSchedule(TrainScheduleViewModel trainScheduleViewModel, CardWithAction cardWithAction);

        void hideDetailSchedule();

        void showReturnTripInfo();

        void hideReturnTripInfo();

        void hideDepartureTripInfo();

        void showDepartureTripInfo();

        String getOriginCity();

        String getDestinationCity();

        void setCityRouteTripInfo(CardWithAction cardWithAction, String originCity, String destinationCity);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getDetailSchedule(String idSchedule, CardWithAction cardWithAction);

        void processInitPassengers(int adultPassengers, int infantPassengers);

        void updateDataPassengers(TrainPassengerViewModel trainPassengerViewModel);
    }
}
