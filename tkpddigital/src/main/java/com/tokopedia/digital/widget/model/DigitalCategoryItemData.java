package com.tokopedia.digital.widget.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryItemData implements Parcelable {
    public static final String DEFAULT_TYPE_DIGITAL = "Digital";
    // TODO : next sprint will be discussed with mojito & wallet team
    public static final int DEFAULT_TOKOCASH_CATEGORY_ID = 103;

    private String name;
    private String imageUrl;
    private String description;
    private String redirectValue;
    private String categoryId;
    private String appLinks;

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
        dest.writeString(this.redirectValue);
        dest.writeString(this.categoryId);
        dest.writeString(this.appLinks);
    }

    public DigitalCategoryItemData() {
    }

    protected DigitalCategoryItemData(Parcel in) {
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.description = in.readString();
        this.redirectValue = in.readString();
        this.categoryId = in.readString();
        this.appLinks = in.readString();
    }

    public static final Parcelable.Creator<DigitalCategoryItemData> CREATOR =
            new Parcelable.Creator<DigitalCategoryItemData>() {
                @Override
                public DigitalCategoryItemData createFromParcel(Parcel source) {
                    return new DigitalCategoryItemData(source);
                }

                @Override
                public DigitalCategoryItemData[] newArray(int size) {
                    return new DigitalCategoryItemData[size];
                }
            };
}
