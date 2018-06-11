package com.tokopedia.train.seat.presentation.viewmodel;

public class TrainSeatViewModel {
    private String column;
    private int row;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
