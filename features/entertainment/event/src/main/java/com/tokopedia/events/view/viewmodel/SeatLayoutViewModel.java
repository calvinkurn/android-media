package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatLayoutViewModel implements Parcelable{

    private List<AreaViewModel> area = null;
    private List<LayoutDetailViewModel> layoutDetail = null;

    public List<AreaViewModel> getArea() {
        return area;
    }

    public void setArea(List<AreaViewModel> area) {
        this.area = area;
    }

    public List<LayoutDetailViewModel> getLayoutDetail() {
        return layoutDetail;
    }

    public void setLayoutDetail(List<LayoutDetailViewModel> layoutDetail) {
        this.layoutDetail = layoutDetail;
    }

    public SeatLayoutViewModel(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (area == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(area);
        }

        if (layoutDetail == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(layoutDetail);
        }
    }

    protected SeatLayoutViewModel(Parcel in) {
        if (in.readByte() == 0x01) {
            area = new ArrayList<AreaViewModel>();
            in.readList(area, AreaViewModel.class.getClassLoader());
        } else {
            area = null;
        }

        if (in.readByte() == 0x01) {
            layoutDetail = new ArrayList<LayoutDetailViewModel>();
            in.readList(layoutDetail, LayoutDetailViewModel.class.getClassLoader());
        } else {
            layoutDetail = null;
        }
    }


    public static final Creator<SeatLayoutViewModel> CREATOR = new Creator<SeatLayoutViewModel>() {
        @Override
        public SeatLayoutViewModel createFromParcel(Parcel source) {
            return new SeatLayoutViewModel(source);
        }

        @Override
        public SeatLayoutViewModel[] newArray(int size) {
            return new SeatLayoutViewModel[size];
        }
    };
}
