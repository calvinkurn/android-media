package com.tokopedia.train.passenger.presentation.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TrainBookingPassengerContract {

    interface View extends CustomerView {

        String getString(@StringRes int resId);

        void renderPassengers(List<TrainPassengerViewModel> trainPassengerViewModels);

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

        Observable<ProfileBuyerInfo> getObservableProfileBuyerInfo();

        String getContactNameEt();

        String getPhoneNumberEt();

        String getEmailEt();

        void setContactName(String contactName);

        void setBirthdate(String birthdate);

        void setPhoneNumber(String phoneNumber);

        void setEmail(String email);
        
        void navigateToChooseSeat(TrainSoftbook trainSoftbook);

        void navigateToReview(TrainSoftbook trainSoftbook);

        void loadPassengerSameAsBuyer(TrainPassengerViewModel trainPassengerViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getProfilBuyer();

        void getDetailSchedule(String idSchedule, CardWithAction cardWithAction);

        void processInitPassengers(int adultPassengers, int infantPassengers);

        void updateDataPassengers(TrainPassengerViewModel trainPassengerViewModel);

        void onSubmitButtonClicked();

        void onChooseSeatButtonClicked();

        void wrapPassengerSameAsBuyer();

        void removePassengerSameAsBuyer();
    }
}
