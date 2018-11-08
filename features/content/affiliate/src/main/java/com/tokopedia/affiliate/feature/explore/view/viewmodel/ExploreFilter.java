package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 10/10/18.
 */
public class ExploreFilter implements Parcelable {
    private String key;
    private String value;

    public ExploreFilter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    public ExploreFilter() {
    }

    protected ExploreFilter(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<ExploreFilter> CREATOR = new Parcelable.Creator<ExploreFilter>() {
        @Override
        public ExploreFilter createFromParcel(Parcel source) {
            return new ExploreFilter(source);
        }

        @Override
        public ExploreFilter[] newArray(int size) {
            return new ExploreFilter[size];
        }
    };
}
