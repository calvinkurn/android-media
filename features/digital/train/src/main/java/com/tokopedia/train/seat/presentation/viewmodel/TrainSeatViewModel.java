package com.tokopedia.train.seat.presentation.viewmodel;

public class TrainSeatViewModel {
    private String column;
    private int row;
    private boolean available;

    public TrainSeatViewModel() {
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrainSeatViewModel && ((TrainSeatViewModel) obj).getRow() == row && ((TrainSeatViewModel) obj).getColumn().equals(column);
    }
}
