package com.tokopedia.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 4/7/17.
 */

public class FilterItemViewModel implements Parcelable{
    private String filterTitleName;
    private boolean isSelected;
    private boolean isActive;

    public FilterItemViewModel(String filterTitleName, boolean isActive) {
        this.filterTitleName = filterTitleName;
        this.isActive = isActive;
        this.isSelected = false;
    }


    protected FilterItemViewModel(Parcel in) {
        filterTitleName = in.readString();
        isSelected = in.readByte() != 0;
        isActive = in.readByte() != 0;
    }

    public static final Creator<FilterItemViewModel> CREATOR = new Creator<FilterItemViewModel>() {
        @Override
        public FilterItemViewModel createFromParcel(Parcel in) {
            return new FilterItemViewModel(in);
        }

        @Override
        public FilterItemViewModel[] newArray(int size) {
            return new FilterItemViewModel[size];
        }
    };

    public String getFilterTitleName() {
        return filterTitleName;
    }

    public void setFilterTitleName(String filterTitleName) {
        this.filterTitleName = filterTitleName;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filterTitleName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isActive ? 1 : 0));
    }
}
