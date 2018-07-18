package com.tokopedia.train.reviewdetail.presentation.model;

public class TrainReviewPassengerInfoViewModelBuilder {

    private String name;
    private String noID;
    private String departureTripClass;
    private String originStationCode;
    private String destinationStationCode;
    private String returnTripClass;
    private String departureSeat;
    private String returnSeat;
    private String passengerType;

    public TrainReviewPassengerInfoViewModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder noID(String noID) {
        this.noID = noID;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder departureTripClass(String departureTripClass) {
        this.departureTripClass = departureTripClass;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder originStationCode(String originStation) {
        this.originStationCode = originStation;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder destinationStationCode(String destinationStation) {
        this.destinationStationCode = destinationStation;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder returnTripClass(String returnTripClass) {
        this.returnTripClass = returnTripClass;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder departureSeat(String departureSeat) {
        this.departureSeat = departureSeat;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder returnSeat(String returnSeat) {
        this.returnSeat = returnSeat;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder passengerType(String passengerType) {
        this.passengerType = passengerType;
        return this;
    }

    public TrainReviewPassengerInfoViewModel createTrainReviewPassengerInfoViewModel() {
        return new TrainReviewPassengerInfoViewModel(name, noID, departureTripClass, originStationCode,
                destinationStationCode, returnTripClass, departureSeat, returnSeat, passengerType);
    }

}