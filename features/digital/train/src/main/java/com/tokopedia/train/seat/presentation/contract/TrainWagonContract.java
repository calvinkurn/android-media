package com.tokopedia.train.seat.presentation.contract;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.List;

public interface TrainWagonContract {

    interface View extends CustomerView{

        TrainWagonViewModel getWagon();

        List<TrainSeatPassengerViewModel> getPassengers();

        void setSelectedSeat(List<TrainSeatViewModel> trainSeatViewModels);

        void renderSeats(List<Visitable> seats);

        void refreshSeats();

        void updatePassengersSeat(TrainSeatPassengerViewModel selectedPassenger, TrainSeatViewModel seat, String wagonCode);

        void updatePreviousSelectionToAvailable(TrainSeatViewModel trainSeatViewModel);

        List<TrainSeatPassengerViewModel> getOriginPassengers();

        List<TrainSeatViewModel> getSelectedSeat();
    }

    interface Presenter extends CustomerPresenter<View>{

        void onViewCreated();

        void onPassengerSeatUpdate();

        void onSeatClicked(TrainSeatPassengerViewModel selectedPassenger, TrainSeatViewModel seat);
    }
}
