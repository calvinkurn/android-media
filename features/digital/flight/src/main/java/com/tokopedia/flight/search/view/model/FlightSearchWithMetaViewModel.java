package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;

import java.util.List;

/**
 * Created by User on 11/17/2017.
 */

public class FlightSearchWithMetaViewModel implements Parcelable {
    private List<FlightSearchViewModel> flightSearchViewModelList;
    private FlightMetaDataDB flightMetaDataDB;

    public FlightSearchWithMetaViewModel(List<FlightSearchViewModel> flightSearchViewModelList, FlightMetaDataDB flightMetaDataDB) {
        this.flightSearchViewModelList = flightSearchViewModelList;
        this.flightMetaDataDB = flightMetaDataDB;
    }

    public List<FlightSearchViewModel> getFlightSearchViewModelList() {
        return flightSearchViewModelList;
    }

    public FlightMetaDataDB getFlightMetaDataDB() {
        return flightMetaDataDB;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.flightSearchViewModelList);
        dest.writeParcelable(this.flightMetaDataDB, flags);
    }

    protected FlightSearchWithMetaViewModel(Parcel in) {
        this.flightSearchViewModelList = in.createTypedArrayList(FlightSearchViewModel.CREATOR);
        this.flightMetaDataDB = in.readParcelable(FlightMetaDataDB.class.getClassLoader());
    }

    public static final Parcelable.Creator<FlightSearchWithMetaViewModel> CREATOR = new Parcelable.Creator<FlightSearchWithMetaViewModel>() {
        @Override
        public FlightSearchWithMetaViewModel createFromParcel(Parcel source) {
            return new FlightSearchWithMetaViewModel(source);
        }

        @Override
        public FlightSearchWithMetaViewModel[] newArray(int size) {
            return new FlightSearchWithMetaViewModel[size];
        }
    };
}
