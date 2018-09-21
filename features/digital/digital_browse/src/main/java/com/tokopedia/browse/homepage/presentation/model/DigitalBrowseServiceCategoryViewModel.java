package com.tokopedia.browse.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapterTypeFactory;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseServiceCategoryViewModel implements Visitable<DigitalBrowseServiceAdapterTypeFactory>, Parcelable {

    private int id;
    private String name;
    private String url;
    private String imageUrl;
    private String type;
    private int categoryId;
    private String appLinks;
    private String categoryLabel;
    private boolean isTitle;

    public DigitalBrowseServiceCategoryViewModel() {
    }

    protected DigitalBrowseServiceCategoryViewModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        imageUrl = in.readString();
        type = in.readString();
        categoryId = in.readInt();
        appLinks = in.readString();
        categoryLabel = in.readString();
        isTitle = in.readByte() != 0;
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
        dest.writeByte((byte) (isTitle ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DigitalBrowseServiceCategoryViewModel> CREATOR = new Creator<DigitalBrowseServiceCategoryViewModel>() {
        @Override
        public DigitalBrowseServiceCategoryViewModel createFromParcel(Parcel in) {
            return new DigitalBrowseServiceCategoryViewModel(in);
        }

        @Override
        public DigitalBrowseServiceCategoryViewModel[] newArray(int size) {
            return new DigitalBrowseServiceCategoryViewModel[size];
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

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    @Override
    public int type(DigitalBrowseServiceAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
