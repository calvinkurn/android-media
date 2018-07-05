package com.tokopedia.train.reviewdetail;

public class TrainReviewPassengerInfoViewModelBuilder {
    private String name;
    private String noID;
    private String departureTripClass;
    private String originStation;
    private String destinationStation;
    private String returnTripClass;
    private String departureSeat;
    private String returnSeat;

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

    public TrainReviewPassengerInfoViewModelBuilder originStation(String originStation) {
        this.originStation = originStation;
        return this;
    }

    public TrainReviewPassengerInfoViewModelBuilder destinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
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

    public TrainReviewPassengerInfoViewModel createTrainReviewPassengerInfoViewModel() {
        return new TrainReviewPassengerInfoViewModel(name, noID, departureTripClass, originStation, destinationStation, returnTripClass, departureSeat, returnSeat);
    }
}