package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CategoriesModel implements Parcelable {

    private int position = -1;
    private int categoryId;
    private String title;
    private String name;
    private String categoryUrl;
    private List<ProductItem> items;
    public final static Parcelable.Creator<CategoriesModel> CREATOR = new Creator<CategoriesModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoriesModel createFromParcel(Parcel in) {
            return new CategoriesModel(in);
        }

        public CategoriesModel[] newArray(int size) {
            return (new CategoriesModel[size]);
        }

    };

    protected CategoriesModel(Parcel in) {
        this.title = in.readString();
        this.name = in.readString();
        this.categoryUrl = in.readString();
        this.position = in.readInt();
        this.categoryId = in.readInt();
        in.readList(this.items, ProductItem.class.getClassLoader());
    }

    public CategoriesModel() {
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

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<ProductItem> getItems() {
        return items;
    }

    public void setItems(List<ProductItem> items) {
        this.items = items;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(name);
        dest.writeString(categoryUrl);
        dest.writeInt(position);
        dest.writeInt(categoryId);
        dest.writeList(items);
    }

    public int describeContents() {
        return 0;
    }

}