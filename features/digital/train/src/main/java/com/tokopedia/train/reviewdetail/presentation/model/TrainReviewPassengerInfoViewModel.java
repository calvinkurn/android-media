package com.tokopedia.train.reviewdetail.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.reviewdetail.presentation.adapter.TrainPassengerAdapterTypeFactory;

/**
 * Created by Rizky on 03/07/18.
 */
public class TrainReviewPassengerInfoViewModel implements Visitable<TrainPassengerAdapterTypeFactory> {

    private String name;
    private String noID;
    private String originStationCode;
    private String destinationStationCode;
    private String departureSeat;
    private String returnSeat;
    private String passengerTypeStr;
    private int passengerType;

    public TrainReviewPassengerInfoViewModel(String name, String noID, String originStationCode,
                                             String destinationStationCode, String departureSeat,
                                             String returnSeat, String passengerTypeStr, int passengerType) {
        this.name = name;
        this.noID = noID;
        this.originStationCode = originStationCode;
        this.destinationStationCode = destinationStationCode;
        this.departureSeat = departureSeat;
        this.returnSeat = returnSeat;
        this.passengerTypeStr = passengerTypeStr;
        this.passengerType = passengerType;
    }

    public String getName() {
        return name;
    }

    public String getNoID() {
        return noID;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public String getDepartureSeat() {
        return departureSeat;
    }

    public String getReturnSeat() {
        return returnSeat;
    }

    public String getPassengerTypeStr() {
        return passengerTypeStr;
    }

    public int getPassengerType() {
        return passengerType;
    }

    @Override
    public int type(TrainPassengerAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
