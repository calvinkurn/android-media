package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class LayoutDetailViewModel implements Parcelable {

    private int rowId;
    private String physicalRowId;
    private List<SeatViewModel> seat = null;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getPhysicalRowId() {
        return physicalRowId;
    }

    public void setPhysicalRowId(String physicalRowId) {
        this.physicalRowId = physicalRowId;
    }

    public List<SeatViewModel> getSeat() {
        return seat;
    }

    public void setSeat(List<SeatViewModel> seat) {
        this.seat = seat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.rowId);
        dest.writeString(this.physicalRowId);
        dest.writeTypedList(this.seat);
    }

    public LayoutDetailViewModel() {
    }

    protected LayoutDetailViewModel(Parcel in) {
        this.rowId = in.readInt();
        this.physicalRowId = in.readString();
        this.seat = in.createTypedArrayList(SeatViewModel.CREATOR);
    }

    public static final Parcelable.Creator<LayoutDetailViewModel> CREATOR = new Parcelable.Creator<LayoutDetailViewModel>() {
        @Override
        public LayoutDetailViewModel createFromParcel(Parcel source) {
            return new LayoutDetailViewModel(source);
        }

        @Override
        public LayoutDetailViewModel[] newArray(int size) {
            return new LayoutDetailViewModel[size];
        }
    };
}
