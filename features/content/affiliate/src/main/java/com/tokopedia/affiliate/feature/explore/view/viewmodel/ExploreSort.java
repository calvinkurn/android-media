package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 10/10/18.
 */
public class ExploreSort implements Parcelable {
    private String key;
    private boolean asc;

    public ExploreSort(String key, boolean asc) {
        this.key = key;
        this.asc = asc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeByte(this.asc ? (byte) 1 : (byte) 0);
    }

    public ExploreSort() {
    }

    protected ExploreSort(Parcel in) {
        this.key = in.readString();
        this.asc = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ExploreSort> CREATOR = new Parcelable.Creator<ExploreSort>() {
        @Override
        public ExploreSort createFromParcel(Parcel source) {
            return new ExploreSort(source);
        }

        @Override
        public ExploreSort[] newArray(int size) {
            return new ExploreSort[size];
        }
    };
}
