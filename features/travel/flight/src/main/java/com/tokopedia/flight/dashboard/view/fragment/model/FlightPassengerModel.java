package com.tokopedia.flight.dashboard.view.fragment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 10/25/17.
 */

public class FlightPassengerModel implements Parcelable, Cloneable {
    private int adult;
    private int children;
    private int infant;

    public FlightPassengerModel() {
    }


    public FlightPassengerModel(int adult, int children, int infant) {
        this.adult = adult;
        this.children = children;
        this.infant = infant;
    }

    protected FlightPassengerModel(Parcel in) {
        adult = in.readInt();
        children = in.readInt();
        infant = in.readInt();
    }

    public static final Creator<FlightPassengerModel> CREATOR = new Creator<FlightPassengerModel>() {
        @Override
        public FlightPassengerModel createFromParcel(Parcel in) {
            return new FlightPassengerModel(in);
        }

        @Override
        public FlightPassengerModel[] newArray(int size) {
            return new FlightPassengerModel[size];
        }
    };

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adult);
        dest.writeInt(children);
        dest.writeInt(infant);
    }

    public static class Builder {
        private int adult;
        private int children;
        private int infant;

        public Builder() {
            adult = 0;
            children = 0;
            infant = 0;
        }

        public FlightPassengerModel.Builder setAdult(int adult) {
            this.adult = adult;
            return this;
        }

        public FlightPassengerModel.Builder setChildren(int children) {
            this.children = children;
            return this;
        }

        public FlightPassengerModel.Builder setInfant(int infant) {
            this.infant = infant;
            return this;
        }

        public FlightPassengerModel build() {
            return new FlightPassengerModel(adult, children, infant);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
