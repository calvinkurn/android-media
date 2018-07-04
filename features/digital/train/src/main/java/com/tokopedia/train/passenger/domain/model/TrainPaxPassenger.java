package com.tokopedia.train.passenger.domain.model;

public class TrainPaxPassenger {
    private String name;
    private String idNumber;
    private int paxType;
    private TrainSeat seat;

    public TrainPaxPassenger(String name, String idNumber, int paxType, TrainSeat seat) {
        this.name = name;
        this.idNumber = idNumber;
        this.paxType = paxType;
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public int getPaxType() {
        return paxType;
    }

    public TrainSeat getSeat() {
        return seat;
    }

    public String getIdNumber() {
        return idNumber;
    }
}
