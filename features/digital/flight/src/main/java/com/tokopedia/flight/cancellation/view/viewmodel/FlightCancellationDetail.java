package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationDetail implements Parcelable {
    private long refundId;
    private String createTime;
    private String estimatedRefund;
    private String realRefund;
    private int status;
    private List<FlightCancellationListPassengerViewModel> passengers;
    private List<FlightOrderJourney> journeys;

    public FlightCancellationDetail() {
    }

    protected FlightCancellationDetail(Parcel in) {
        refundId = in.readLong();
        createTime = in.readString();
        estimatedRefund = in.readString();
        realRefund = in.readString();
        status = in.readInt();
        passengers = in.createTypedArrayList(FlightCancellationListPassengerViewModel.CREATOR);
        journeys = in.createTypedArrayList(FlightOrderJourney.CREATOR);
    }

    public static final Creator<FlightCancellationDetail> CREATOR = new Creator<FlightCancellationDetail>() {
        @Override
        public FlightCancellationDetail createFromParcel(Parcel in) {
            return new FlightCancellationDetail(in);
        }

        @Override
        public FlightCancellationDetail[] newArray(int size) {
            return new FlightCancellationDetail[size];
        }
    };

    public long getRefundId() {
        return refundId;
    }

    public void setRefundId(long refundId) {
        this.refundId = refundId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEstimatedRefund() {
        return estimatedRefund;
    }

    public void setEstimatedRefund(String estimatedRefund) {
        this.estimatedRefund = estimatedRefund;
    }

    public String getRealRefund() {
        return realRefund;
    }

    public void setRealRefund(String realRefund) {
        this.realRefund = realRefund;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FlightCancellationListPassengerViewModel> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<FlightCancellationListPassengerViewModel> passengers) {
        this.passengers = passengers;
    }

    public List<FlightOrderJourney> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<FlightOrderJourney> journeys) {
        this.journeys = journeys;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(refundId);
        parcel.writeString(createTime);
        parcel.writeString(estimatedRefund);
        parcel.writeString(realRefund);
        parcel.writeInt(status);
        parcel.writeTypedList(passengers);
        parcel.writeTypedList(journeys);
    }
}
