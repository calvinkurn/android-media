package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 3/29/18.
 */

public class FlightCancellationWrapperModel implements Parcelable {
    private FlightCancellationReasonAndAttachmentModel cancellationReasonAndAttachment;
    private List<FlightCancellationModel> getCancellations;
    private String invoice;

    public FlightCancellationWrapperModel() {
    }

    protected FlightCancellationWrapperModel(Parcel in) {
        cancellationReasonAndAttachment = in.readParcelable(FlightCancellationReasonAndAttachmentModel.class.getClassLoader());
        getCancellations = in.createTypedArrayList(FlightCancellationModel.CREATOR);
        invoice = in.readString();
    }

    public static final Creator<FlightCancellationWrapperModel> CREATOR = new Creator<FlightCancellationWrapperModel>() {
        @Override
        public FlightCancellationWrapperModel createFromParcel(Parcel in) {
            return new FlightCancellationWrapperModel(in);
        }

        @Override
        public FlightCancellationWrapperModel[] newArray(int size) {
            return new FlightCancellationWrapperModel[size];
        }
    };

    public List<FlightCancellationModel> getGetCancellations() {
        return getCancellations;
    }

    public void setGetCancellations(List<FlightCancellationModel> getCancellations) {
        this.getCancellations = getCancellations;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public FlightCancellationReasonAndAttachmentModel getCancellationReasonAndAttachment() {
        return cancellationReasonAndAttachment;
    }

    public void setCancellationReasonAndAttachment(FlightCancellationReasonAndAttachmentModel cancellationReasonAndAttachment) {
        this.cancellationReasonAndAttachment = cancellationReasonAndAttachment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(cancellationReasonAndAttachment, i);
        parcel.writeTypedList(getCancellations);
        parcel.writeString(invoice);
    }
}
