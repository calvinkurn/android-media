package com.tokopedia.flight.dashboard.view.fragment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.dashboard.view.adapter.FlightClassesAdapterTypeFactory;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassModel implements Visitable<FlightClassesAdapterTypeFactory>, Parcelable {
    public static final Creator<FlightClassModel> CREATOR = new Creator<FlightClassModel>() {
        @Override
        public FlightClassModel createFromParcel(Parcel in) {
            return new FlightClassModel(in);
        }

        @Override
        public FlightClassModel[] newArray(int size) {
            return new FlightClassModel[size];
        }
    };
    private String title;
    private int id;

    public FlightClassModel() {
    }

    public FlightClassModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    protected FlightClassModel(Parcel in) {
        title = in.readString();
        id = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
    }

    @Override
    public int type(FlightClassesAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
