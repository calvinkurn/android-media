package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author  by alvarisi on 3/26/18.
 */

public class FlightCancellationReasonAndAttachmentModel implements Parcelable{

    private List<FlightCancellationAttachmentModel> attachments;
    private String reason;
    private String reasonId;
    private long estimateRefund;
    private String estimateFmt;
    private boolean showEstimateRefund;

    public FlightCancellationReasonAndAttachmentModel() {
    }

    protected FlightCancellationReasonAndAttachmentModel(Parcel in) {
        attachments = in.createTypedArrayList(FlightCancellationAttachmentModel.CREATOR);
        reason = in.readString();
        reasonId = in.readString();
        estimateRefund = in.readLong();
        estimateFmt = in.readString();
        showEstimateRefund = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(attachments);
        dest.writeString(reason);
        dest.writeString(reasonId);
        dest.writeLong(estimateRefund);
        dest.writeString(estimateFmt);
        dest.writeByte((byte) (showEstimateRefund ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationReasonAndAttachmentModel> CREATOR = new Creator<FlightCancellationReasonAndAttachmentModel>() {
        @Override
        public FlightCancellationReasonAndAttachmentModel createFromParcel(Parcel in) {
            return new FlightCancellationReasonAndAttachmentModel(in);
        }

        @Override
        public FlightCancellationReasonAndAttachmentModel[] newArray(int size) {
            return new FlightCancellationReasonAndAttachmentModel[size];
        }
    };

    public List<FlightCancellationAttachmentModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FlightCancellationAttachmentModel> attachments) {
        this.attachments = attachments;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getEstimateRefund() {
        return estimateRefund;
    }

    public void setEstimateRefund(long estimateRefund) {
        this.estimateRefund = estimateRefund;
    }

    public String getEstimateFmt() {
        return estimateFmt;
    }

    public void setEstimateFmt(String estimateFmt) {
        this.estimateFmt = estimateFmt;
    }

    public boolean isShowEstimateRefund() {
        return showEstimateRefund;
    }

    public void setShowEstimateRefund(boolean showEstimateRefund) {
        this.showEstimateRefund = showEstimateRefund;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }
}
