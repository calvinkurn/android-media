package com.tokopedia.train.seat.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.Date;
import java.util.List;

public interface TrainSeatContract {
    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void renderWagon(List<TrainWagonViewModel> trainWagonViewModels);

        void hidePage();

        void showPage();

        void showErrorGetSeatMaps(String message);

        String getExpireDate();

        void renderExpireDateCountdown(Date expireDate);

        void backToHomePage();

        List<TrainSeatPassengerViewModel> getPassengers();

        List<TrainSeatPassengerViewModel> getOriginalPassenger();

        void navigateToReview(List<TrainSeatPassengerViewModel> originalPassenger);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getSeatMaps();

        void onRunningOutOfTime();

        void onWagonChooserClicked();

        void onSubmitButtonClicked();
    }
}
