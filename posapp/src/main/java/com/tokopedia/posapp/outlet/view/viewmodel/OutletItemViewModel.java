package com.tokopedia.posapp.outlet.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by okasurya on 8/1/17.
 */

public class OutletItemViewModel implements Parcelable {
    private String outletId;
    private String outletName;
    private String outletAddres;
    private String outletPhone;

    public OutletItemViewModel(String id, String name, String address, String phone) {
        this.outletId = id;
        this.outletName = name;
        this.outletAddres = address;
        this.outletPhone = phone;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletAddres() {
        return outletAddres;
    }

    public void setOutletAddres(String outletAddres) {
        this.outletAddres = outletAddres;
    }

    public String getOutletPhone() {
        return outletPhone;
    }

    public void setOutletPhone(String outletPhone) {
        this.outletPhone = outletPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.outletId);
        dest.writeString(this.outletName);
        dest.writeString(this.outletAddres);
        dest.writeString(this.outletPhone);
    }

    protected OutletItemViewModel(Parcel in) {
        this.outletId = in.readString();
        this.outletName = in.readString();
        this.outletAddres = in.readString();
        this.outletPhone = in.readString();
    }

    public static final Creator<OutletItemViewModel> CREATOR = new Creator<OutletItemViewModel>() {
        @Override
        public OutletItemViewModel createFromParcel(Parcel source) {
            return new OutletItemViewModel(source);
        }

        @Override
        public OutletItemViewModel[] newArray(int size) {
            return new OutletItemViewModel[size];
        }
    };
}
