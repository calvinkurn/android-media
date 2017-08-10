package com.tokopedia.digital.widget.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/7/17.
 */

public class DigitalCategoryItemHeader implements Parcelable {

    private String title;
    private String siteUrl;
    private int resIconId;

    private DigitalCategoryItemHeader(Builder builder) {
        setTitle(builder.title);
        setSiteUrl(builder.siteUrl);
        setResIconId(builder.resIconId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public int getResIconId() {
        return resIconId;
    }

    public void setResIconId(int resIconId) {
        this.resIconId = resIconId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.siteUrl);
        dest.writeInt(this.resIconId);
    }

    public DigitalCategoryItemHeader() {
    }

    protected DigitalCategoryItemHeader(Parcel in) {
        this.title = in.readString();
        this.siteUrl = in.readString();
        this.resIconId = in.readInt();
    }

    public static final Creator<DigitalCategoryItemHeader> CREATOR
            = new Creator<DigitalCategoryItemHeader>() {
        @Override
        public DigitalCategoryItemHeader createFromParcel(Parcel source) {
            return new DigitalCategoryItemHeader(source);
        }

        @Override
        public DigitalCategoryItemHeader[] newArray(int size) {
            return new DigitalCategoryItemHeader[size];
        }
    };


    public static final class Builder {
        private String title;
        private String siteUrl;
        private int resIconId;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder siteUrl(String val) {
            siteUrl = val;
            return this;
        }

        public Builder resIconId(int val) {
            resIconId = val;
            return this;
        }

        public DigitalCategoryItemHeader build() {
            return new DigitalCategoryItemHeader(this);
        }
    }
}
