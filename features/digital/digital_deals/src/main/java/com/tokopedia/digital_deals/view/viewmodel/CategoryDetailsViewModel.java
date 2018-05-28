package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailsViewModel implements Parcelable {


    private List<CategoryItemsViewModel> items = null;
    private List<BrandViewModel> brands = null;
    private PageViewModel page;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(page);
        dest.writeList(this.items);
        dest.writeList(this.brands);
    }

    protected CategoryDetailsViewModel(Parcel in) {

        this.items = new ArrayList<CategoryItemsViewModel>();
        this.brands = new ArrayList<BrandViewModel>();
        this.page = ((PageViewModel) in.readValue((PageViewModel.class.getClassLoader())));
        in.readList(this.items, CategoryItemsViewModel.class.getClassLoader());
        in.readList(this.brands, BrandViewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryDetailsViewModel> CREATOR = new Parcelable.Creator<CategoryDetailsViewModel>() {
        @Override
        public CategoryDetailsViewModel createFromParcel(Parcel source) {
            return new CategoryDetailsViewModel(source);
        }

        @Override
        public CategoryDetailsViewModel[] newArray(int size) {
            return new CategoryDetailsViewModel[size];
        }
    };
}
