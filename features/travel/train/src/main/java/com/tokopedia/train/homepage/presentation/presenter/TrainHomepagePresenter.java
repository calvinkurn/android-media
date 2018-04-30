package com.tokopedia.train.homepage.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.train.homepage.presentation.model.TrainPassengerViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;

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

    void onOriginStationChanged(TrainStationViewModel viewModel);

    void onDepartureStationChanged(TrainStationViewModel viewModel);

    void onSubmitButtonClicked();

    void onTrainPassengerChange(TrainPassengerViewModel passengerViewModel);

}
