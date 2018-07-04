package com.tokopedia.train.passenger.domain.model;

public class TrainSeat {
    private String klass;
    private String wagonNo;
    private String row;
    private String column;

    public TrainSeat(String klass, String wagonNo, String row, String column) {
        this.klass = klass;
        this.wagonNo = wagonNo;
        this.row = row;
        this.column = column;
    }

    public String getKlass() {
        return klass;
    }

    public String getWagonNo() {
        return wagonNo;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }
}
