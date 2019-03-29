package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 02/10/18.
 */
public class DashboardFloatingButtonViewModel implements Parcelable {

    String text;
    int count;

    public DashboardFloatingButtonViewModel(String text, int count) {
        this.text = text;
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.count);
    }

    protected DashboardFloatingButtonViewModel(Parcel in) {
        this.text = in.readString();
        this.count = in.readInt();
    }

    public static final Parcelable.Creator<DashboardFloatingButtonViewModel> CREATOR = new Parcelable.Creator<DashboardFloatingButtonViewModel>() {
        @Override
        public DashboardFloatingButtonViewModel createFromParcel(Parcel source) {
            return new DashboardFloatingButtonViewModel(source);
        }

        @Override
        public DashboardFloatingButtonViewModel[] newArray(int size) {
            return new DashboardFloatingButtonViewModel[size];
        }
    };
}
