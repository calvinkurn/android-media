package com.tokopedia.browse.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * @author by furqan on 07/09/18.
 */

public class DigitalBrowseServiceViewModel implements Parcelable {

    private List<DigitalBrowseServiceCategoryViewModel> categoryViewModelList;
    private Map<String, IndexPositionModel> titleMap;

    public DigitalBrowseServiceViewModel() {
    }

    protected DigitalBrowseServiceViewModel(Parcel in) {
        categoryViewModelList = in.createTypedArrayList(DigitalBrowseServiceCategoryViewModel.CREATOR);
    }

    public static final Creator<DigitalBrowseServiceViewModel> CREATOR = new Creator<DigitalBrowseServiceViewModel>() {
        @Override
        public DigitalBrowseServiceViewModel createFromParcel(Parcel in) {
            return new DigitalBrowseServiceViewModel(in);
        }

        @Override
        public DigitalBrowseServiceViewModel[] newArray(int size) {
            return new DigitalBrowseServiceViewModel[size];
        }
    };

    public List<DigitalBrowseServiceCategoryViewModel> getCategoryViewModelList() {
        return categoryViewModelList;
    }

    public void setCategoryViewModelList(List<DigitalBrowseServiceCategoryViewModel> categoryViewModelList) {
        this.categoryViewModelList = categoryViewModelList;
    }

    public Map<String, IndexPositionModel> getTitleMap() {
        return titleMap;
    }

    public void setTitleMap(Map<String, IndexPositionModel> titleMap) {
        this.titleMap = titleMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(categoryViewModelList);
    }
}
