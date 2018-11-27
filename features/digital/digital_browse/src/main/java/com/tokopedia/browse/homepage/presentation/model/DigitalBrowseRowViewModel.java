package com.tokopedia.browse.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory;

/**
 * @author by furqan on 05/09/18.
 */

public class DigitalBrowseRowViewModel implements Parcelable, Visitable<DigitalBrowseMarketplaceAdapterTypeFactory> {

    private int id;
    private String name;
    private String url;
    private String imageUrl;
    private String type;
    private int categoryId;
    private String appLinks;
    private String categoryLabel;

    public DigitalBrowseRowViewModel() {
    }

    protected DigitalBrowseRowViewModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        imageUrl = in.readString();
        type = in.readString();
        categoryId = in.readInt();
        appLinks = in.readString();
        categoryLabel = in.readString();
    }

    public static final Creator<DigitalBrowseRowViewModel> CREATOR = new Creator<DigitalBrowseRowViewModel>() {
        @Override
        public DigitalBrowseRowViewModel createFromParcel(Parcel in) {
            return new DigitalBrowseRowViewModel(in);
        }

        @Override
        public DigitalBrowseRowViewModel[] newArray(int size) {
            return new DigitalBrowseRowViewModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(String appLinks) {
        this.appLinks = appLinks;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeString(type);
        dest.writeInt(categoryId);
        dest.writeString(appLinks);
        dest.writeString(categoryLabel);
    }

    @Override
    public int type(DigitalBrowseMarketplaceAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
