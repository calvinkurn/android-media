package com.tokopedia.train.homepage.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.train.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPassengerViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;

/**
 * @author Rizky on 21/02/18.
 */

public interface TrainHomepagePresenter extends CustomerPresenter<TrainHomepageView> {

    void roundTrip();

    void singleTrip();

    void onDepartureDateButtonClicked();

    void onReturnDateButtonClicked();

    void onDepartureDateChange(int year, int month, int dayOfMonth);

    void onReturnDateChange(int year, int month, int dayOfMonth);

    void initialize();

    void onOriginStationChanged(TrainStationAndCityViewModel viewModel);

    void onDepartureStationChanged(TrainStationAndCityViewModel viewModel);

    void onSubmitButtonClicked();

    void onTrainPassengerChange(TrainPassengerViewModel passengerViewModel);

    void onSavedStateAvailable(TrainHomepageViewModel viewModel);

    void onReverseStationButtonClicked();

    void saveHomepageViewModelToCache(TrainHomepageViewModel viewModel);

    void onLoginRecieved();

    void getTrainPromoList();

    void onDestroy();

}
