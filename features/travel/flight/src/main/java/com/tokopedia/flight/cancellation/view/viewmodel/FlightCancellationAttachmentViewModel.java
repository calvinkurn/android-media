package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory;

/**
 * Created by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentViewModel implements Visitable<FlightCancellationAttachmentTypeFactory>, Parcelable{
    private String filename;
    private String filepath;
    private String imageurl;
    private String passengerName;
    private long percentageUpload;

    public FlightCancellationAttachmentViewModel() {
    }

    protected FlightCancellationAttachmentViewModel(Parcel in) {
        filename = in.readString();
        filepath = in.readString();
        imageurl = in.readString();
        passengerName = in.readString();
        percentageUpload = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filename);
        dest.writeString(filepath);
        dest.writeString(imageurl);
        dest.writeString(passengerName);
        dest.writeLong(percentageUpload);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationAttachmentViewModel> CREATOR = new Creator<FlightCancellationAttachmentViewModel>() {
        @Override
        public FlightCancellationAttachmentViewModel createFromParcel(Parcel in) {
            return new FlightCancellationAttachmentViewModel(in);
        }

        @Override
        public FlightCancellationAttachmentViewModel[] newArray(int size) {
            return new FlightCancellationAttachmentViewModel[size];
        }
    };

    @Override
    public int type(FlightCancellationAttachmentTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public long getPercentageUpload() {
        return percentageUpload;
    }

    public void setPercentageUpload(long percentageUpload) {
        this.percentageUpload = percentageUpload;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightCancellationAttachmentViewModel && (
                ((FlightCancellationAttachmentViewModel) obj).getFilepath().equalsIgnoreCase(filepath) ||
                        (((FlightCancellationAttachmentViewModel) obj).getImageurl() != null &&
                                ((FlightCancellationAttachmentViewModel) obj).getImageurl().equalsIgnoreCase(imageurl)
                        )
        );
    }

}
