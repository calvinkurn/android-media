package com.tokopedia.train.seat.presentation.viewmodel;

import java.util.List;

public class TrainSeatRowColumnViewModel {
    private List<TrainSeatViewModel> seats;
    private int maxColumn = 0;
    private int maxRow = 0;

    public TrainSeatRowColumnViewModel(List<TrainSeatViewModel> seats, int maxColumn, int maxRow) {
        this.seats = seats;
        this.maxColumn = maxColumn;
        this.maxRow = maxRow;
    }

    public List<TrainSeatViewModel> getSeats() {
        return seats;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public int getMaxRow() {
        return maxRow;
    }
}
