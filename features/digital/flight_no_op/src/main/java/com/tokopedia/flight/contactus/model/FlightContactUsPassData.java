package com.tokopedia.flight.contactus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightContactUsPassData implements Parcelable {
    public static final Creator<FlightContactUsPassData> CREATOR = new Creator<FlightContactUsPassData>() {
        @Override
        public FlightContactUsPassData createFromParcel(Parcel in) {
            return new FlightContactUsPassData(in);
        }

        @Override
        public FlightContactUsPassData[] newArray(int size) {
            return new FlightContactUsPassData[size];
        }
    };
    private String toolbarTitle;
    private String solutionId;
    private String orderId;
    private String descriptionTitle;
    private String attachmentTitle;
    private String description;

    public FlightContactUsPassData() {
    }

    protected FlightContactUsPassData(Parcel in) {
        toolbarTitle = in.readString();
        solutionId = in.readString();
        orderId = in.readString();
        descriptionTitle = in.readString();
        attachmentTitle = in.readString();
        description = in.readString();
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDescriptionTitle() {
        return descriptionTitle;
    }

    public void setDescriptionTitle(String descriptionTitle) {
        this.descriptionTitle = descriptionTitle;
    }

    public String getAttachmentTitle() {
        return attachmentTitle;
    }

    public void setAttachmentTitle(String attachmentTitle) {
        this.attachmentTitle = attachmentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(toolbarTitle);
        parcel.writeString(solutionId);
        parcel.writeString(orderId);
        parcel.writeString(descriptionTitle);
        parcel.writeString(attachmentTitle);
        parcel.writeString(description);
    }
}
