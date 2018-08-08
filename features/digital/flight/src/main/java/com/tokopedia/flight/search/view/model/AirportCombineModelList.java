package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/20/2017.
 */

public class AirportCombineModelList implements Parcelable {
    private ArrayList<FlightAirportCombineModel> data = new ArrayList<>();

    public AirportCombineModelList() {
    }

    public ArrayList<FlightAirportCombineModel> getData() {
        return data;
    }

    public FlightAirportCombineModel getData(String depAirport, String arrAirport) {
        for (int i = 0, sizei = data.size(); i<sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = data.get(i);
            if (flightAirportCombineModel.getDepAirport().equals(depAirport) &&
                    flightAirportCombineModel.getArrAirport().equals(arrAirport)){
                return flightAirportCombineModel;
            }
        }
        return null;
    }

    public AirportCombineModelList(List<String> departureAirportList, List<String> arrivalAirportList) {
        for (int i = 0, sizei = departureAirportList.size(); i < sizei; i++) {
            for (int j = 0, sizej = arrivalAirportList.size(); j < sizej; j++) {
                data.add(new FlightAirportCombineModel(departureAirportList.get(i),
                                arrivalAirportList.get(j)));
            }
        }
    }

    // not retrieving data yet and still need refresh, then it needs to retrieve data.
    public boolean isRetrievingData(){
        for (int i = 0, sizei = data.size(); i<sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = data.get(i);
            if (!flightAirportCombineModel.isHasLoad() || flightAirportCombineModel.isNeedRefresh()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
    }

    protected AirportCombineModelList(Parcel in) {
        this.data = in.createTypedArrayList(FlightAirportCombineModel.CREATOR);
    }

    public static final Parcelable.Creator<AirportCombineModelList> CREATOR = new Parcelable.Creator<AirportCombineModelList>() {
        @Override
        public AirportCombineModelList createFromParcel(Parcel source) {
            return new AirportCombineModelList(source);
        }

        @Override
        public AirportCombineModelList[] newArray(int size) {
            return new AirportCombineModelList[size];
        }
    };
}
