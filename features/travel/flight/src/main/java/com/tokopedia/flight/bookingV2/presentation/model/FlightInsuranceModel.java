package com.tokopedia.flight.bookingV2.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FlightInsuranceModel implements Parcelable{
    private String id;
    private String name;
    private String description;
    private long totalPrice;
    private boolean defaultChecked;
    private String tncAggreement;
    private String tncUrl;
    private List<FlightInsuranceBenefitModel> benefits;

    public FlightInsuranceModel() {
    }

    protected FlightInsuranceModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        totalPrice = in.readLong();
        defaultChecked = in.readByte() != 0;
        tncAggreement = in.readString();
        tncUrl = in.readString();
        benefits = in.createTypedArrayList(FlightInsuranceBenefitModel.CREATOR);
    }

    public static final Creator<FlightInsuranceModel> CREATOR = new Creator<FlightInsuranceModel>() {
        @Override
        public FlightInsuranceModel createFromParcel(Parcel in) {
            return new FlightInsuranceModel(in);
        }

        @Override
        public FlightInsuranceModel[] newArray(int size) {
            return new FlightInsuranceModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isDefaultChecked() {
        return defaultChecked;
    }

    public void setDefaultChecked(boolean defaultChecked) {
        this.defaultChecked = defaultChecked;
    }

    public String getTncAggreement() {
        return tncAggreement;
    }

    public void setTncAggreement(String tncAggreement) {
        this.tncAggreement = tncAggreement;
    }

    public String getTncUrl() {
        return tncUrl;
    }

    public void setTncUrl(String tncUrl) {
        this.tncUrl = tncUrl;
    }

    public List<FlightInsuranceBenefitModel> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<FlightInsuranceBenefitModel> benefits) {
        this.benefits = benefits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeLong(totalPrice);
        parcel.writeByte((byte) (defaultChecked ? 1 : 0));
        parcel.writeString(tncAggreement);
        parcel.writeString(tncUrl);
        parcel.writeTypedList(benefits);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightInsuranceModel && ((FlightInsuranceModel) obj).getId().equals(getId());
    }
}
