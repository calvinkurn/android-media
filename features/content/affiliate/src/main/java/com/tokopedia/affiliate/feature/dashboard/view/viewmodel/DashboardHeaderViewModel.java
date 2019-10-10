package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardHeaderViewModel implements Parcelable {

    private String totalSaldoAktif;
    private String affiliateIncome;
    private String seenCount;
    private String clickCount;
    private String buyCount;
    private String productCount;

    public DashboardHeaderViewModel(String totalSaldoAktif, String affiliateIncome, String seenCount, String clickCount, String buyCount, String productCount) {
        this.totalSaldoAktif = totalSaldoAktif;
        this.affiliateIncome = affiliateIncome;
        this.seenCount = seenCount;
        this.clickCount = clickCount;
        this.buyCount = buyCount;
        this.productCount = productCount;
    }

    public String getTotalSaldoAktif() {
        return totalSaldoAktif;
    }

    public void setTotalSaldoAktif(String totalSaldoAktif) {
        this.totalSaldoAktif = totalSaldoAktif;
    }

    public String getAffiliateIncome() {
        return affiliateIncome;
    }

    public void setAffiliateIncome(String affiliateIncome) {
        this.affiliateIncome = affiliateIncome;
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

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.affiliateIncome);
        dest.writeString(this.seenCount);
        dest.writeString(this.clickCount);
        dest.writeString(this.buyCount);
    }

    protected DashboardHeaderViewModel(Parcel in) {
        this.affiliateIncome = in.readString();
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
