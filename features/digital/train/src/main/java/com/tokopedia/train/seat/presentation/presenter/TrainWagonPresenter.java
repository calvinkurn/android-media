package com.tokopedia.train.seat.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.seat.presentation.contract.TrainWagonContract;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatTopLabelViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainWagonPresenter extends BaseDaggerPresenter<TrainWagonContract.View> implements TrainWagonContract.Presenter {
    String[] seating = new String[]{"A", "B", "C", "D", "E"};

    @Inject
    public TrainWagonPresenter() {
    }

    private List<TrainSeatViewModel> transformToSelectedSeat(List<TrainSeatPassengerViewModel> passengers) {
        List<TrainSeatViewModel> seats = new ArrayList<>();
        for (TrainSeatPassengerViewModel passenger : passengers) {
            if (getView().getWagon().getWagonCode().equalsIgnoreCase(passenger.getSeatViewModel().getWagonCode())) {
                TrainSeatViewModel seat = new TrainSeatViewModel();
                seat.setRow(Integer.parseInt(passenger.getSeatViewModel().getRow()));
                seat.setColumn(passenger.getSeatViewModel().getColumn());
                seat.setAvailable(false);
                seats.add(seat);
            }
        }
        return seats;
    }

    @Override
    public void onViewCreated() {
        getView().setSelectedSeat(transformToSelectedSeat(getView().getPassengers()));
        List<Visitable> visitables = new ArrayList<>();
        visitables.addAll(buildTrainSeatLabel(getView().getWagon().getMaxColumn()));
        visitables.addAll(getView().getWagon().getSeats());
        getView().renderSeats(visitables);
    }

    private List<Visitable> buildTrainSeatLabel(int maxColumn) {
        List<Visitable> visitables = new ArrayList<>();
        TrainSeatTopLabelViewModel viewModel;
        for (int i = 0; i < maxColumn; i++) {
            viewModel = new TrainSeatTopLabelViewModel(seating[i]);
            visitables.add(viewModel);
        }
        return visitables;
    }

    @Override
    public void onPassengerSeatUpdate() {
        getView().setSelectedSeat(transformToSelectedSeat(getView().getPassengers()));
        getView().refreshSeats();
    }

    @Override
    public void onSeatClicked(TrainSeatPassengerViewModel selectedPassenger, TrainSeatViewModel seat) {
        List<TrainSeatPassengerViewModel> passengers = getView().getPassengers();
        boolean isFilledByExistingPassenger = false;
        for (TrainSeatPassengerViewModel passenger : passengers) {
            if (getView().getWagon().getWagonCode().equalsIgnoreCase(passenger.getSeatViewModel().getWagonCode()) &&
                    passenger.getSeatViewModel().getRow().equalsIgnoreCase(String.valueOf(seat.getRow())) &&
                    passenger.getSeatViewModel().getColumn().equalsIgnoreCase(String.valueOf(seat.getColumn()))) {
                isFilledByExistingPassenger = true;
                break;
            }
        }
        if (!isFilledByExistingPassenger) {
            getView().updatePassengersSeat(selectedPassenger, seat, getView().getWagon().getWagonCode());
        }
    }
}
