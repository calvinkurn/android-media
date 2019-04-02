package com.tokopedia.train.seat.domain.model;

import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerSeatViewModel;

public class TrainPassengerSeat {
    private String name;
    private TrainSeatPassengerSeatViewModel seat;

    public TrainPassengerSeat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrainSeatPassengerSeatViewModel getSeat() {
        return seat;
    }

    public void setSeat(TrainSeatPassengerSeatViewModel seat) {
        this.seat = seat;
    }
}
