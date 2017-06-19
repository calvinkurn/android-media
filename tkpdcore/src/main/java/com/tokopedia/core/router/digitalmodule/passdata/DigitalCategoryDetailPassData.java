package com.tokopedia.core.router.digitalmodule.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class DigitalCategoryDetailPassData implements Parcelable {
    public static final String PARAM_CATEGORY_ID = "category_id";
    private String categoryId;
    private String url;
    private String appLinks;
    private String categoryName;

    private DigitalCategoryDetailPassData(Builder builder) {
        setCategoryId(builder.categoryId);
        setUrl(builder.url);
        setAppLinks(builder.appLinks);
        setCategoryName(builder.categoryName);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(String appLinks) {
        this.appLinks = appLinks;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.url);
        dest.writeString(this.appLinks);
        dest.writeString(this.categoryName);
    }

    public DigitalCategoryDetailPassData() {
    }

    protected DigitalCategoryDetailPassData(Parcel in) {
        this.categoryId = in.readString();
        this.url = in.readString();
        this.appLinks = in.readString();
        this.categoryName = in.readString();
    }

    public static final Creator<DigitalCategoryDetailPassData> CREATOR =
            new Creator<DigitalCategoryDetailPassData>() {
                @Override
                public DigitalCategoryDetailPassData createFromParcel(Parcel source) {
                    return new DigitalCategoryDetailPassData(source);
                }

                @Override
                public DigitalCategoryDetailPassData[] newArray(int size) {
                    return new DigitalCategoryDetailPassData[size];
                }
            };


    public static final class Builder {
        private String categoryId;
        private String url;
        private String appLinks;
        private String categoryName;

        public Builder() {
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder appLinks(String val) {
            appLinks = val;
            return this;
        }

        public Builder categoryName(String val) {
            categoryName = val;
            return this;
        }

        public DigitalCategoryDetailPassData build() {
            return new DigitalCategoryDetailPassData(this);
        }
    }
}
