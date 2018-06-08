package com.tokopedia.train.seat.presentation.viewmodel;

public class TrainSeatPassengerViewModel {
    private int passengerNumber;
    private int salutationId;
    private int paxType;
    private String name;
    private String number;
    private String birthdate;
    private String phone;
    private TrainSeatPassengerSeatViewModel seatViewModel;

    public int getSalutationId() {
        return salutationId;
    }

    public void setSalutationId(int salutationId) {
        this.salutationId = salutationId;
    }

    public int getPaxType() {
        return paxType;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TrainSeatPassengerSeatViewModel getSeatViewModel() {
        return seatViewModel;
    }

    public void setSeatViewModel(TrainSeatPassengerSeatViewModel seatViewModel) {
        this.seatViewModel = seatViewModel;
    }

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(int passengerNumber) {
        this.passengerNumber = passengerNumber;
    }
}
