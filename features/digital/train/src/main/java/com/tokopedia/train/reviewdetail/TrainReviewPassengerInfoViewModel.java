package com.tokopedia.train.reviewdetail;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * Created by Rizky on 03/07/18.
 */
public class TrainReviewPassengerInfoViewModel implements Visitable<TrainPassengerAdapterTypeFactory> {
    private String name;
    private String noID;
    private String departureTripClass;
    private String originStation;
    private String destinationStation;
    private String returnTripClass;
    private String departureSeat;
    private String returnSeat;

    public TrainReviewPassengerInfoViewModel(String name, String noID, String departureTripClass,
                                             String originStation, String destinationStation,
                                             String returnTripClass, String departureSeat, String returnSeat) {
        this.name = name;
        this.noID = noID;
        this.departureTripClass = departureTripClass;
        this.returnTripClass = returnTripClass;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureSeat = departureSeat;
        this.returnSeat = returnSeat;
    }

    public String getName() {
        return name;
    }

    public String getNoID() {
        return noID;
    }

    public String getDepartureTripClass() {
        return departureTripClass;
    }

    public String getOriginStation() {
        return originStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public String getReturnTripClass() {
        return returnTripClass;
    }

    public String getDepartureSeat() {
        return departureSeat;
    }

    public String getReturnSeat() {
        return returnSeat;
    }

    @Override
    public int type(TrainPassengerAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
