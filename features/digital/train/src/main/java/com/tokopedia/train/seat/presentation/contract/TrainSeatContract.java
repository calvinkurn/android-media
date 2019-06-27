package com.tokopedia.train.seat.presentation.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.Date;
import java.util.List;

public interface TrainSeatContract {
    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void stopTrace();

        void renderWagon(List<TrainWagonViewModel> trainWagonViewModels, int i);

        void hidePage();

        void showPage();

        void showErrorGetSeatMaps(String message);

        String getExpireDate();

        void renderExpireDateCountdown(Date expireDate);

        void showExpiredPaymentDialog();

        List<TrainSeatPassengerViewModel> getPassengers();

        List<TrainSeatPassengerViewModel> getOriginalPassenger();

        void navigateToReview(TrainSoftbook trainSoftbook);

        TrainSoftbook getTrainSoftbook();

        boolean isReturning();

        void setOriginPassenger(List<TrainSeatPassengerViewModel> originPassengers);

        void setPassengers(List<TrainSeatPassengerViewModel> originPassengers);

        void updateSelectedWagon();

        void showWagonChooser();

        String getString(@StringRes int resId);

        void setToolbarSubTitle(String subtitle);

        void showConfirmChangePassengersDialog(List<TrainSeatPassengerViewModel> passengers);

        void clearSeatMaps();

        void showErrorChangeSeat(String message);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getSeatMaps();

        void onRunningOutOfTime();

        void onWagonChooserClicked();

        void onSubmitButtonClicked();

        void onViewCreated();

        void onPassengerSeatChange(TrainSeatPassengerViewModel passenger,
                                   TrainSeatViewModel seat,
                                   String wagonCode);

        void onChangePassengerConfirmDialogAccepted();
    }
}
