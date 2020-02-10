package com.tokopedia.flight.bookingV2.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightInsuranceBenefitViewModel implements Parcelable{
    private String title;
    private String description;
    private String icon;

    public FlightInsuranceBenefitViewModel() {
    }

    protected FlightInsuranceBenefitViewModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        icon = in.readString();
    }

    public static final Creator<FlightInsuranceBenefitViewModel> CREATOR = new Creator<FlightInsuranceBenefitViewModel>() {
        @Override
        public FlightInsuranceBenefitViewModel createFromParcel(Parcel in) {
            return new FlightInsuranceBenefitViewModel(in);
        }

        @Override
        public FlightInsuranceBenefitViewModel[] newArray(int size) {
            return new FlightInsuranceBenefitViewModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(icon);
    }
}
