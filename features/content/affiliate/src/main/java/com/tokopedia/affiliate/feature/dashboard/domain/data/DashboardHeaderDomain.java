package com.tokopedia.affiliate.feature.dashboard.domain.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardHeaderDomain implements Parcelable {
    private String saldoString;
    private String seenCount;
    private String clickCount;
    private String buyCount;

    public DashboardHeaderDomain(String saldoString, String seenCount, String clickCount, String buyCount) {
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

    protected DashboardHeaderDomain(Parcel in) {
        this.saldoString = in.readString();
        this.seenCount = in.readString();
        this.clickCount = in.readString();
        this.buyCount = in.readString();
    }

    public static final Parcelable.Creator<DashboardHeaderDomain> CREATOR = new Parcelable.Creator<DashboardHeaderDomain>() {
        @Override
        public DashboardHeaderDomain createFromParcel(Parcel source) {
            return new DashboardHeaderDomain(source);
        }

        @Override
        public DashboardHeaderDomain[] newArray(int size) {
            return new DashboardHeaderDomain[size];
        }
    };
}
