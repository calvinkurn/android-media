package com.tokopedia.filter.newdynamicfilter.analytics;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterTrackingData implements Parcelable {
    private String event;
    private String filterCategory;
    private String categoryId;
    private String prefix;

    public FilterTrackingData(String event, String filterCategory, String categoryId, String prefix) {
        this.event = event;
        this.filterCategory = filterCategory;
        this.categoryId = categoryId;
        this.prefix = prefix;
    }

    public String getEvent() {
        return event;
    }

    public String getFilterCategory() {
        return filterCategory;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
        dest.writeString(this.filterCategory);
        dest.writeString(this.categoryId);
        dest.writeString(this.prefix);
    }

    protected FilterTrackingData(Parcel in) {
        this.event = in.readString();
        this.filterCategory = in.readString();
        this.categoryId = in.readString();
        this.prefix = in.readString();
    }

    public static final Creator<FilterTrackingData> CREATOR = new Creator<FilterTrackingData>() {
        @Override
        public FilterTrackingData createFromParcel(Parcel source) {
            return new FilterTrackingData(source);
        }

        @Override
        public FilterTrackingData[] newArray(int size) {
            return new FilterTrackingData[size];
        }
    };
}
