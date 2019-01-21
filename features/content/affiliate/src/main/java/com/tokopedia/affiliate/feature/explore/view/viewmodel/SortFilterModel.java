package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 28/12/18.
 */
public class SortFilterModel implements Parcelable {

    private List<FilterViewModel> filterList;

    public SortFilterModel(List<FilterViewModel> filterList) {
        this.filterList = filterList;
    }

    public SortFilterModel() {
        this.filterList = new ArrayList<>();
    }

    public List<FilterViewModel> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FilterViewModel> filterList) {
        this.filterList = filterList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.filterList);
    }

    protected SortFilterModel(Parcel in) {
        this.filterList = in.createTypedArrayList(FilterViewModel.CREATOR);
    }

    public static final Creator<SortFilterModel> CREATOR = new Creator<SortFilterModel>() {
        @Override
        public SortFilterModel createFromParcel(Parcel source) {
            return new SortFilterModel(source);
        }

        @Override
        public SortFilterModel[] newArray(int size) {
            return new SortFilterModel[size];
        }
    };
}
