package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/16/2017.
 */

public class FlightAirportCombineModel implements Parcelable{
    private String depAirport;
    private String arrAirport;
    private boolean hasLoad;
    private boolean needRefresh;
    private int noOfRetry = 0;
    private List<String> airlines;

    public FlightAirportCombineModel(String depAirport, String arrAirport) {
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.hasLoad = false;
        this.needRefresh = true;
        this.noOfRetry = 0;
        this.airlines = new ArrayList<>();
    }

    protected FlightAirportCombineModel(Parcel in) {
        depAirport = in.readString();
        arrAirport = in.readString();
        hasLoad = in.readByte() != 0;
        needRefresh = in.readByte() != 0;
        noOfRetry = in.readInt();
        airlines = in.createStringArrayList();
    }

    public static final Creator<FlightAirportCombineModel> CREATOR = new Creator<FlightAirportCombineModel>() {
        @Override
        public FlightAirportCombineModel createFromParcel(Parcel in) {
            return new FlightAirportCombineModel(in);
        }

        @Override
        public FlightAirportCombineModel[] newArray(int size) {
            return new FlightAirportCombineModel[size];
        }
    };

    public String getDepAirport() {
        return depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public boolean isHasLoad() {
        return hasLoad;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public void setHasLoad(boolean hasLoad) {
        this.hasLoad = hasLoad;
    }

    public int getNoOfRetry() {
        return noOfRetry;
    }

    public void setNoOfRetry(int noOfRetry) {
        this.noOfRetry = noOfRetry;
    }

    public List<String> getAirlines() {
        return airlines;
    }

    public void setAirlines(List<String> airlines) {
        this.airlines = airlines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(depAirport);
        parcel.writeString(arrAirport);
        parcel.writeByte((byte) (hasLoad ? 1 : 0));
        parcel.writeByte((byte) (needRefresh ? 1 : 0));
        parcel.writeInt(noOfRetry);
        parcel.writeStringList(airlines);
    }
}
