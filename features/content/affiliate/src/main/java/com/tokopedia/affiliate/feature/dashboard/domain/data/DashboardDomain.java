package com.tokopedia.affiliate.feature.dashboard.domain.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardDomain implements Parcelable {
    private DashboardHeaderDomain header;
    private List<DashboardItemDomain> itemList;
    private String recommendationLeftString;

    public DashboardDomain(DashboardHeaderDomain header, List<DashboardItemDomain> itemList, String recommendationLeftString) {
        this.header = header;
        this.itemList = itemList;
        this.recommendationLeftString = recommendationLeftString;
    }

    public DashboardHeaderDomain getHeader() {
        return header;
    }

    public void setHeader(DashboardHeaderDomain header) {
        this.header = header;
    }

    public List<DashboardItemDomain> getItemList() {
        return itemList;
    }

    public void setItemList(List<DashboardItemDomain> itemList) {
        this.itemList = itemList;
    }

    public String getRecommendationLeftString() {
        return recommendationLeftString;
    }

    public void setRecommendationLeftString(String recommendationLeftString) {
        this.recommendationLeftString = recommendationLeftString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.header, flags);
        dest.writeList(this.itemList);
        dest.writeString(this.recommendationLeftString);
    }

    protected DashboardDomain(Parcel in) {
        this.header = in.readParcelable(DashboardHeaderDomain.class.getClassLoader());
        this.itemList = new ArrayList<DashboardItemDomain>();
        in.readList(this.itemList, DashboardItemDomain.class.getClassLoader());
        this.recommendationLeftString = in.readString();
    }

    public static final Creator<DashboardDomain> CREATOR = new Creator<DashboardDomain>() {
        @Override
        public DashboardDomain createFromParcel(Parcel source) {
            return new DashboardDomain(source);
        }

        @Override
        public DashboardDomain[] newArray(int size) {
            return new DashboardDomain[size];
        }
    };
}