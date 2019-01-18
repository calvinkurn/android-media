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
    private List <SortViewModel> sortList;

    public SortFilterModel(List<FilterViewModel> filterList, List<SortViewModel> sortList) {
        this.filterList = filterList;
        this.sortList = sortList;
    }

    public SortFilterModel() {
        this.filterList = new ArrayList<>();
        this.sortList = new ArrayList<>();
    }

    public List<FilterViewModel> getFilterList() {
        return filterList;
    }

    public List<SortViewModel> getSortList() {
        return sortList;
    }

    public void setSortList(List<SortViewModel> sortList) {
        this.sortList = sortList;
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
        dest.writeTypedList(this.sortList);
    }

    protected SortFilterModel(Parcel in) {
        this.filterList = in.createTypedArrayList(FilterViewModel.CREATOR);
        this.sortList = in.createTypedArrayList(SortViewModel.CREATOR);
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
