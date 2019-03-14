package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class CategoryItem implements Parcelable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category_url")
    @Expose
    private String categoryUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("items")
    @Expose
    private List<ProductItem> items = null;
    @SerializedName("id")
    @Expose
    private int categoryId;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("is_card")
    @Expose
    private int isCard;

    public CategoryItem() {
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

    public List<ProductItem> getItems() {
        return items;
    }

    public void setItems(List<ProductItem> items) {
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

    public int getIsCard() {
        return isCard;
    }

    public void setIsCard(int isCard) {
        this.isCard = isCard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeString(this.categoryUrl);
        dest.writeString(this.url);
        dest.writeString(this.mediaUrl);
        dest.writeList(this.items);
        dest.writeInt(categoryId);
        dest.writeInt(count);
        dest.writeInt(isCard);
    }

    protected CategoryItem(Parcel in) {
        this.title = in.readString();
        this.name = in.readString();
        this.categoryUrl = in.readString();
        this.url = in.readString();
        this.items = new ArrayList<ProductItem>();
        this.mediaUrl = in.readString();
        in.readList(this.items, ProductItem.class.getClassLoader());
        this.categoryId = in.readInt();
        this.count = in.readInt();
        this.isCard = in.readInt();
    }

    public static final Parcelable.Creator<CategoryItem> CREATOR = new Parcelable.Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel source) {
            return new CategoryItem(source);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };
}
