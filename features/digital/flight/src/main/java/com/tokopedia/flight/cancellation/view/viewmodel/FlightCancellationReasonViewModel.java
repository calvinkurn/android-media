package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationReasonAdapterTypeFactory;

import java.util.List;

/**
 * @author by furqan on 26/10/18.
 */

public class FlightCancellationReasonViewModel implements Parcelable, Visitable<FlightCancellationReasonAdapterTypeFactory> {

    private String id;
    private String detail;
    private List<String> requiredDocs;

    public FlightCancellationReasonViewModel() {
    }


    protected FlightCancellationReasonViewModel(Parcel in) {
        id = in.readString();
        detail = in.readString();
        requiredDocs = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(detail);
        dest.writeStringList(requiredDocs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationReasonViewModel> CREATOR = new Creator<FlightCancellationReasonViewModel>() {
        @Override
        public FlightCancellationReasonViewModel createFromParcel(Parcel in) {
            return new FlightCancellationReasonViewModel(in);
        }

        @Override
        public FlightCancellationReasonViewModel[] newArray(int size) {
            return new FlightCancellationReasonViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getRequiredDocs() {
        return requiredDocs;
    }

    public void setRequiredDocs(List<String> requiredDocs) {
        this.requiredDocs = requiredDocs;
    }

    @Override
    public int type(FlightCancellationReasonAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
