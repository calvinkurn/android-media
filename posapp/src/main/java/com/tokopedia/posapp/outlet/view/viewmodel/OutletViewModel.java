package com.tokopedia.posapp.outlet.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletViewModel implements Parcelable {
    private List<OutletItemViewModel> outletList;
    private String nextUri;
    private String prevUri;

    public List<OutletItemViewModel> getOutletList() {
        return outletList;
    }

    public void setOutletList(List<OutletItemViewModel> outletList) {
        this.outletList = outletList;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

    public String getPrevUri() {
        return prevUri;
    }

    public void setPrevUri(String prevUri) {
        this.prevUri = prevUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.outletList);
        dest.writeString(this.nextUri);
        dest.writeString(this.prevUri);
    }

    public OutletViewModel() {
    }

    protected OutletViewModel(Parcel in) {
        this.outletList = in.createTypedArrayList(OutletItemViewModel.CREATOR);
        this.nextUri = in.readString();
        this.prevUri = in.readString();
    }

    public static final Parcelable.Creator<OutletViewModel> CREATOR = new Parcelable.Creator<OutletViewModel>() {
        @Override
        public OutletViewModel createFromParcel(Parcel source) {
            return new OutletViewModel(source);
        }

        @Override
        public OutletViewModel[] newArray(int size) {
            return new OutletViewModel[size];
        }
    };
}
