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
    private List<CategoryItemsViewModel> items = null;

    public CategoryViewModel(String title, String name, String url, String mediaUrl, List<CategoryItemsViewModel> items) {
        this.title = title;
        this.name = name;
        this.items = items;
        this.mediaUrl=mediaUrl;
        this.url = url;
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
    }

    protected CategoryViewModel(Parcel in) {
        this.title = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.items = new ArrayList<CategoryItemsViewModel>();
        this.mediaUrl = in.readString();
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
