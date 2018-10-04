package com.tokopedia.digital.categorylist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryItemData implements Parcelable{
    public static final String DEFAULT_TYPE_DIGITAL = "Digital";
    // TODO : next sprint will be discussed with mojito & wallet team
    public static final int DEFAULT_TOKOCASH_CATEGORY_ID = 103;

    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private String redirectValue;
    private String categoryId;
    private String appLinks;

    protected DigitalCategoryItemData(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        redirectValue = in.readString();
        categoryId = in.readString();
        appLinks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(redirectValue);
        dest.writeString(categoryId);
        dest.writeString(appLinks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DigitalCategoryItemData> CREATOR = new Creator<DigitalCategoryItemData>() {
        @Override
        public DigitalCategoryItemData createFromParcel(Parcel in) {
            return new DigitalCategoryItemData(in);
        }

        @Override
        public DigitalCategoryItemData[] newArray(int size) {
            return new DigitalCategoryItemData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRedirectValue() {
        return redirectValue;
    }

    public void setRedirectValue(String redirectValue) {
        this.redirectValue = redirectValue;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(String appLinks) {
        this.appLinks = appLinks;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof DigitalCategoryItemData && ((DigitalCategoryItemData) obj).getName().equals(getName());
    }

    public DigitalCategoryItemData() {
    }

}
