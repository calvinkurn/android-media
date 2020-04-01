package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.opportunity.viewmodel.SortingTypeViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityFilterViewModel implements Parcelable {
    private ArrayList<SortingTypeViewModel> listSortingType;
    private ArrayList<FilterViewModel> listFilter;

    public OpportunityFilterViewModel() {

    }

    private OpportunityFilterViewModel(Parcel in) {
        listSortingType = in.createTypedArrayList(SortingTypeViewModel.CREATOR);
        listFilter = in.createTypedArrayList(FilterViewModel.CREATOR);
    }

    public static final Creator<OpportunityFilterViewModel> CREATOR = new Creator<OpportunityFilterViewModel>() {
        @Override
        public OpportunityFilterViewModel createFromParcel(Parcel in) {
            return new OpportunityFilterViewModel(in);
        }

        @Override
        public OpportunityFilterViewModel[] newArray(int size) {
            return new OpportunityFilterViewModel[size];
        }
    };

    public ArrayList<SortingTypeViewModel> getListSortingType() {
        return listSortingType;
    }

    public void setListSortingType(ArrayList<SortingTypeViewModel> listSortingType) {
        this.listSortingType = listSortingType;
    }

    public ArrayList<FilterViewModel> getListFilter() {
        return listFilter;
    }

    public void setListFilter(ArrayList<FilterViewModel> listFilter) {
        this.listFilter = listFilter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listSortingType);
        dest.writeTypedList(listFilter);
    }
}
