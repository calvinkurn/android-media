package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class CategoryViewModel implements Parcelable {
    private String title;
    private String name;
    private String url;
    private String mediaUrl;
    private int categoryId;
    private int count;
    private List<CategoryItemsViewModel> items = null;

    public CategoryViewModel() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public List<CategoryItemsViewModel> getItems() {
        return items;
    }

    public void setItems(List<CategoryItemsViewModel> items) {
        this.items = items;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.mediaUrl);
        dest.writeList(this.items);
        dest.writeInt(categoryId);
        dest.writeInt(count);
    }

    protected CategoryViewModel(Parcel in) {
        this.title = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.items = new ArrayList<CategoryItemsViewModel>();
        this.mediaUrl = in.readString();
        in.readList(this.items, CategoryItemsViewModel.class.getClassLoader());
        this.categoryId = in.readInt();
        this.count = in.readInt();
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
