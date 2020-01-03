package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderDeadlineViewModel implements Parcelable {
    private String deadlineProcessDayLeft;
    private String deadlineProcessHourLeft;
    private String deadlineProcess;
    private String deadlinePoProcessDayLeft;
    private String deadlineShippingDayLeft;
    private String deadlineShippingHourLeft;
    private String deadlineShipping;
    private String deadlineFinishDayLeft;
    private String deadlineFinishHourLeft;
    private String deadlineFinishDate;
    private String deadlineColor;

    public OrderDeadlineViewModel() {
    }

    protected OrderDeadlineViewModel(Parcel in) {
        deadlineProcessDayLeft = in.readString();
        deadlineProcessHourLeft = in.readString();
        deadlineProcess = in.readString();
        deadlinePoProcessDayLeft = in.readString();
        deadlineShippingDayLeft = in.readString();
        deadlineShippingHourLeft = in.readString();
        deadlineShipping = in.readString();
        deadlineFinishDayLeft = in.readString();
        deadlineFinishHourLeft = in.readString();
        deadlineFinishDate = in.readString();
        deadlineColor = in.readString();
    }

    public static final Creator<OrderDeadlineViewModel> CREATOR = new Creator<OrderDeadlineViewModel>() {
        @Override
        public OrderDeadlineViewModel createFromParcel(Parcel in) {
            return new OrderDeadlineViewModel(in);
        }

        @Override
        public OrderDeadlineViewModel[] newArray(int size) {
            return new OrderDeadlineViewModel[size];
        }
    };

    public String getDeadlineProcessDayLeft() {
        return deadlineProcessDayLeft;
    }

    public void setDeadlineProcessDayLeft(String deadlineProcessDayLeft) {
        this.deadlineProcessDayLeft = deadlineProcessDayLeft;
    }

    public String getDeadlineProcessHourLeft() {
        return deadlineProcessHourLeft;
    }

    public void setDeadlineProcessHourLeft(String deadlineProcessHourLeft) {
        this.deadlineProcessHourLeft = deadlineProcessHourLeft;
    }

    public String getDeadlineProcess() {
        return deadlineProcess;
    }

    public void setDeadlineProcess(String deadlineProcess) {
        this.deadlineProcess = deadlineProcess;
    }

    public String getDeadlinePoProcessDayLeft() {
        return deadlinePoProcessDayLeft;
    }

    public void setDeadlinePoProcessDayLeft(String deadlinePoProcessDayLeft) {
        this.deadlinePoProcessDayLeft = deadlinePoProcessDayLeft;
    }

    public String getDeadlineShippingDayLeft() {
        return deadlineShippingDayLeft;
    }

    public void setDeadlineShippingDayLeft(String deadlineShippingDayLeft) {
        this.deadlineShippingDayLeft = deadlineShippingDayLeft;
    }

    public String getDeadlineShippingHourLeft() {
        return deadlineShippingHourLeft;
    }

    public void setDeadlineShippingHourLeft(String deadlineShippingHourLeft) {
        this.deadlineShippingHourLeft = deadlineShippingHourLeft;
    }

    public String getDeadlineShipping() {
        return deadlineShipping;
    }

    public void setDeadlineShipping(String deadlineShipping) {
        this.deadlineShipping = deadlineShipping;
    }

    public String getDeadlineFinishDayLeft() {
        return deadlineFinishDayLeft;
    }

    public void setDeadlineFinishDayLeft(String deadlineFinishDayLeft) {
        this.deadlineFinishDayLeft = deadlineFinishDayLeft;
    }

    public String getDeadlineFinishHourLeft() {
        return deadlineFinishHourLeft;
    }

    public void setDeadlineFinishHourLeft(String deadlineFinishHourLeft) {
        this.deadlineFinishHourLeft = deadlineFinishHourLeft;
    }

    public String getDeadlineFinishDate() {
        return deadlineFinishDate;
    }

    public void setDeadlineFinishDate(String deadlineFinishDate) {
        this.deadlineFinishDate = deadlineFinishDate;
    }

    public String getDeadlineColor() {
        return deadlineColor;
    }

    public void setDeadlineColor(String deadlineColor) {
        this.deadlineColor = deadlineColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deadlineProcessDayLeft);
        dest.writeString(deadlineProcessHourLeft);
        dest.writeString(deadlineProcess);
        dest.writeString(deadlinePoProcessDayLeft);
        dest.writeString(deadlineShippingDayLeft);
        dest.writeString(deadlineShippingHourLeft);
        dest.writeString(deadlineShipping);
        dest.writeString(deadlineFinishDayLeft);
        dest.writeString(deadlineFinishHourLeft);
        dest.writeString(deadlineFinishDate);
        dest.writeString(deadlineColor);
    }
}
