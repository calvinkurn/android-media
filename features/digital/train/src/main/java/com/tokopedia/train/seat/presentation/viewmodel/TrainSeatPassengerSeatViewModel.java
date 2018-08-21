package com.tokopedia.train.seat.presentation.viewmodel;

public class TrainSeatPassengerSeatViewModel {
    private String wagonCode;
    private String row;
    private String column;

    public TrainSeatPassengerSeatViewModel() {
    }

    public String getWagonCode() {
        return wagonCode;
    }

    public void setWagonCode(String wagonCode) {
        this.wagonCode = wagonCode;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrainSeatPassengerSeatViewModel &&
                ((TrainSeatPassengerSeatViewModel) obj).getWagonCode().equals(wagonCode) &&
                ((TrainSeatPassengerSeatViewModel) obj).getColumn().equals(column) &&
                ((TrainSeatPassengerSeatViewModel) obj).getRow().equals(row);
    }
}
