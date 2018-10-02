package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 18/09/18.
 */
public class EmptyDashboardViewModel implements Visitable<DashboardItemTypeFactory>,Parcelable {

    private int count;

    public EmptyDashboardViewModel(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
        dest.writeInt(this.count);
    }

    protected EmptyDashboardViewModel(Parcel in) {
        this.count = in.readInt();
    }

    public static final Parcelable.Creator<EmptyDashboardViewModel> CREATOR = new Parcelable.Creator<EmptyDashboardViewModel>() {
        @Override
        public EmptyDashboardViewModel createFromParcel(Parcel source) {
            return new EmptyDashboardViewModel(source);
        }

        @Override
        public EmptyDashboardViewModel[] newArray(int size) {
            return new EmptyDashboardViewModel[size];
        }
    };
}
