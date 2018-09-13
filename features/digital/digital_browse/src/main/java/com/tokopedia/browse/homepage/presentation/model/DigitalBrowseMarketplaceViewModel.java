package com.tokopedia.browse.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowseMarketplaceViewModel implements Parcelable {

    private List<DigitalBrowsePopularBrandsViewModel> popularBrandsList;
    private List<DigitalBrowseRowViewModel> rowViewModelList;

    public DigitalBrowseMarketplaceViewModel() {
    }

    protected DigitalBrowseMarketplaceViewModel(Parcel in) {
        popularBrandsList = in.createTypedArrayList(DigitalBrowsePopularBrandsViewModel.CREATOR);
        rowViewModelList = in.createTypedArrayList(DigitalBrowseRowViewModel.CREATOR);
    }

    public static final Creator<DigitalBrowseMarketplaceViewModel> CREATOR = new Creator<DigitalBrowseMarketplaceViewModel>() {
        @Override
        public DigitalBrowseMarketplaceViewModel createFromParcel(Parcel in) {
            return new DigitalBrowseMarketplaceViewModel(in);
        }

        @Override
        public DigitalBrowseMarketplaceViewModel[] newArray(int size) {
            return new DigitalBrowseMarketplaceViewModel[size];
        }
    };

    public List<DigitalBrowsePopularBrandsViewModel> getPopularBrandsList() {
        return popularBrandsList;
    }

    public void setPopularBrandsList(List<DigitalBrowsePopularBrandsViewModel> popularBrandsList) {
        this.popularBrandsList = popularBrandsList;
    }

    public List<DigitalBrowseRowViewModel> getRowViewModelList() {
        return rowViewModelList;
    }

    public void setRowViewModelList(List<DigitalBrowseRowViewModel> rowViewModelList) {
        this.rowViewModelList = rowViewModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(popularBrandsList);
        dest.writeTypedList(rowViewModelList);
    }
}
