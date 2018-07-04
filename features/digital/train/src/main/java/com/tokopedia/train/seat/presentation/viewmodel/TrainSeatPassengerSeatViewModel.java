package com.tokopedia.train.seat.presentation.viewmodel;

public class TrainSeatPassengerSeatViewModel {
    private String wagonCode;
    private String classSeat;
    private String row;
    private String column;

    public TrainSeatPassengerSeatViewModel() {
    }

    public TrainSeatPassengerSeatViewModel(String wagonCode, String classSeat, String row, String column) {
        this.wagonCode = wagonCode;
        this.classSeat = classSeat;
        this.row = row;
        this.column = column;
    }

    public String getWagonCode() {
        return wagonCode;
    }

    public void setWagonCode(String wagonCode) {
        this.wagonCode = wagonCode;
    }

    public String getClassSeat() {
        return classSeat;
    }

    public void setClassSeat(String classSeat) {
        this.classSeat = classSeat;
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
