package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class CategoryViewModel implements Parcelable {
    private String title;
    private String name;
    private List<CategoryItemsViewModel> items = null;

    public CategoryViewModel(String title, String name, List<CategoryItemsViewModel> items) {
        this.title=title;
        this.name=name;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryItemsViewModel> getItems() {
        return items;
    }

    public void setItems(List<CategoryItemsViewModel> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeList(this.items);
    }

    protected CategoryViewModel(Parcel in) {
        this.title = in.readString();
        this.name = in.readString();
        this.items = new ArrayList<CategoryItemsViewModel>();
        in.readList(this.items, CategoryItemsViewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryViewModel> CREATOR = new Parcelable.Creator<CategoryViewModel>() {
        @Override
        public CategoryViewModel createFromParcel(Parcel source) {
            return new CategoryViewModel(source);
        }

        @Override
        public CategoryViewModel[] newArray(int size) {
            return new CategoryViewModel[size];
        }
    };
}
