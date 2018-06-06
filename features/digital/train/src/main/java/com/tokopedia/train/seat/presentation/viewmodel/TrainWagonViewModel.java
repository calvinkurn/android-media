package com.tokopedia.train.seat.presentation.viewmodel;

import java.util.List;

public class TrainWagonViewModel {
    private String wagonCode;
    private List<TrainSeatViewModel> seats;

    public TrainWagonViewModel() {
    }

    public String getWagonCode() {
        return wagonCode;
    }

    public void setWagonCode(String wagonCode) {
        this.wagonCode = wagonCode;
    }

    public List<TrainSeatViewModel> getSeats() {
        return seats;
    }

    public void setSeats(List<TrainSeatViewModel> seats) {
        this.seats = seats;
    }
}
