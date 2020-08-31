package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.orderlist.data.cloud.entity.RefundDetailEntity;
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
    private List<FlightCancellationListPassengerModel> passengers;
    private List<FlightOrderJourney> journeys;
    private String statusStr;
    private String statusType;
    private String refundInfo;
    private RefundDetailEntity refundDetail;

    public FlightCancellationDetail() {
    }

    protected FlightCancellationDetail(Parcel in) {
        refundId = in.readLong();
        createTime = in.readString();
        estimatedRefund = in.readString();
        realRefund = in.readString();
        status = in.readInt();
        passengers = in.createTypedArrayList(FlightCancellationListPassengerModel.CREATOR);
        journeys = in.createTypedArrayList(FlightOrderJourney.CREATOR);
        statusStr = in.readString();
        statusType = in.readString();
        refundInfo = in.readString();
        refundDetail = in.readParcelable(RefundDetailEntity.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(refundId);
        dest.writeString(createTime);
        dest.writeString(estimatedRefund);
        dest.writeString(realRefund);
        dest.writeInt(status);
        dest.writeTypedList(passengers);
        dest.writeTypedList(journeys);
        dest.writeString(statusStr);
        dest.writeString(statusType);
        dest.writeString(refundInfo);
        dest.writeParcelable(refundDetail, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public List<FlightCancellationListPassengerModel> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<FlightCancellationListPassengerModel> passengers) {
        this.passengers = passengers;
    }

    public List<FlightOrderJourney> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<FlightOrderJourney> journeys) {
        this.journeys = journeys;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getRefundInfo() {
        return refundInfo;
    }

    public void setRefundInfo(String refundInfo) {
        this.refundInfo = refundInfo;
    }

    public RefundDetailEntity getRefundDetail() {
        return refundDetail;
    }

    public void setRefundDetail(RefundDetailEntity refundDetail) {
        this.refundDetail = refundDetail;
    }
}
