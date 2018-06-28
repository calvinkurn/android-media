package com.tokopedia.train.passenger.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainBookingPassengerPresenter extends BaseDaggerPresenter<TrainBookingPassengerContract.View>
        implements TrainBookingPassengerContract.Presenter {

    @Inject
    public TrainBookingPassengerPresenter() {
    }

    @Override
    public void processInitPassengers(int adultPassengers, int infantPassengers) {
        List<TrainPassengerViewModel> passengerViewModelList = initPassenger(adultPassengers, infantPassengers);
        getView().setCurrentListPassenger(passengerViewModelList);
        getView().renderPassenger(passengerViewModelList);
    }

    @Override
    public void updateDataPassengers(TrainPassengerViewModel trainPassengerViewModel) {
        List<TrainPassengerViewModel> passengerViewModels = getView().getCurrentPassengerList();
        int indexPassenger = passengerViewModels.indexOf(trainPassengerViewModel);
        if (indexPassenger != -1) {
            passengerViewModels.set(indexPassenger, trainPassengerViewModel);
        }
        getView().renderPassenger(passengerViewModels);
    }

    private List<TrainPassengerViewModel> initPassenger(int adultPassengers, int infantPassengers) {
        List<TrainPassengerViewModel> trainPassengerViewModelList = new ArrayList<>();
        int passengerId = 1;
        for (int i = 1; i <= adultPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setPassengerId(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                            i, getView().getString(R.string.train_select_passenger_adult_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        for (int i = 1; i <= infantPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setPassengerId(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.INFANT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                            i, getView().getString(R.string.train_select_passenger_infant_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        return trainPassengerViewModelList;
    }

    private String formatPassengerHeader(String prefix, int number, String postix) {
        return String.format(getView().getString(R.string.train_passenger_header_format),
                prefix,
                number,
                postix
        );
    }
}
