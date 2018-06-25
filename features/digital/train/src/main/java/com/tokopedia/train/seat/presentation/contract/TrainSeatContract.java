package com.tokopedia.train.seat.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.List;

public interface TrainSeatContract {
    interface View extends CustomerView {

        void showGetSeatMapLoading();

        void hideGetSeatMapLoading();

        void renderWagon(List<TrainWagonViewModel> trainWagonViewModels);

        void hidePage();

        void showPage();

        void showErrorGetSeatMaps(String message);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getSeatMaps();
    }
}
