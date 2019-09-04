package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardHeaderViewModel implements Visitable<DashboardItemTypeFactory>,Parcelable {

    private String saldoString;
    private String seenCount;
    private String clickCount;
    private String buyCount;

    public DashboardHeaderViewModel(String saldoString, String seenCount, String clickCount, String buyCount) {
        this.saldoString = saldoString;
        this.seenCount = seenCount;
        this.clickCount = clickCount;
        this.buyCount = buyCount;
    }

    public String getSaldoString() {
        return saldoString;
    }

    public void setSaldoString(String saldoString) {
        this.saldoString = saldoString;
    }

    public String getSeenCount() {
        return seenCount;
    }

    public void setSeenCount(String seenCount) {
        this.seenCount = seenCount;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    @Override
    public int type(DashboardItemTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.saldoString);
        dest.writeString(this.seenCount);
        dest.writeString(this.clickCount);
        dest.writeString(this.buyCount);
    }

    protected DashboardHeaderViewModel(Parcel in) {
        this.saldoString = in.readString();
        this.seenCount = in.readString();
        this.clickCount = in.readString();
        this.buyCount = in.readString();
    }

    public static final Parcelable.Creator<DashboardHeaderViewModel> CREATOR = new Parcelable.Creator<DashboardHeaderViewModel>() {
        @Override
        public DashboardHeaderViewModel createFromParcel(Parcel source) {
            return new DashboardHeaderViewModel(source);
        }

        @Override
        public DashboardHeaderViewModel[] newArray(int size) {
            return new DashboardHeaderViewModel[size];
        }
    };
}
