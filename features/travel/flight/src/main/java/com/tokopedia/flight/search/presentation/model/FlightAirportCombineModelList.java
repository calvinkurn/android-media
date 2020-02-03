package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by furqan on 02/10/18.
 */

public class FlightAirportCombineModelList implements Parcelable {

    private List<FlightAirportCombineModel> data = new ArrayList<>();

    public FlightAirportCombineModelList() {
    }

    public FlightAirportCombineModelList(List<String> departureAirportList, List<String> arrivalAirportList) {
        for (String depAirport : departureAirportList) {
            for (String arrAirport : arrivalAirportList) {
                data.add(new FlightAirportCombineModel(depAirport, arrAirport));
            }
        }
    }

    public List<FlightAirportCombineModel> getData() {
        return data;
    }

    public FlightAirportCombineModel getData(String depAirport, String arrAirport) {
        for (int i = 0, sizei = data.size(); i < sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = data.get(i);
            if (flightAirportCombineModel.getDepAirport().equals(depAirport) &&
                    flightAirportCombineModel.getArrAirport().equals(arrAirport)) {
                return flightAirportCombineModel;
            }
        }
        return null;
    }

    public FlightAirportCombineModel getSingleData(String departureAirport, String arrivalAirport) {
        for (FlightAirportCombineModel singleData : data) {
            if (singleData.getDepAirport().equals(departureAirport) &&
                    singleData.getArrAirport().equals(arrivalAirport)) {
                return singleData;
            }
        }

        return null;
    }

    /**
     * check did all of the data received?
     * <p>
     * not yet? then retrieve the rest
     * done? good job
     *
     * @return
     */
    public boolean isRetrievingData() {
        for (FlightAirportCombineModel singleData : data) {
            if (!singleData.isHasLoad() || singleData.isNeedRefresh()) {
                return true;
            }
        }
        return false;
    }

    protected FlightAirportCombineModelList(Parcel in) {
        data = in.createTypedArrayList(FlightAirportCombineModel.CREATOR);
    }

    public static final Creator<FlightAirportCombineModelList> CREATOR = new Creator<FlightAirportCombineModelList>() {
        @Override
        public FlightAirportCombineModelList createFromParcel(Parcel in) {
            return new FlightAirportCombineModelList(in);
        }

        @Override
        public FlightAirportCombineModelList[] newArray(int size) {
            return new FlightAirportCombineModelList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }
}
