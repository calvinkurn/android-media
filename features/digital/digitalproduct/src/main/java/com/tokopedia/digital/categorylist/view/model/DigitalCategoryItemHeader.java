package com.tokopedia.digital.categorylist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/7/17.
 */

public class DigitalCategoryItemHeader implements Parcelable {
    public enum TypeMenu {
        TRANSACTION, SUBSCRIPTION, FAVORITE_NUMBER
    }

    private String title;
    private String siteUrl;
    private int resIconId;
    private TypeMenu typeMenu;

    private DigitalCategoryItemHeader(Builder builder) {
        setTitle(builder.title);
        setSiteUrl(builder.siteUrl);
        setResIconId(builder.resIconId);
        setTypeMenu(builder.typeMenu);
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

    public TypeMenu getTypeMenu() {
        return typeMenu;
    }

    public void setTypeMenu(TypeMenu typeMenu) {
        this.typeMenu = typeMenu;
    }

    public DigitalCategoryItemHeader() {
    }


    public static final class Builder {
        private String title;
        private String siteUrl;
        private int resIconId;
        private TypeMenu typeMenu;

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

        public Builder typeMenu(TypeMenu val) {
            typeMenu = val;
            return this;
        }

        public DigitalCategoryItemHeader build() {
            return new DigitalCategoryItemHeader(this);
        }
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
        dest.writeInt(this.typeMenu == null ? -1 : this.typeMenu.ordinal());
    }

    protected DigitalCategoryItemHeader(Parcel in) {
        this.title = in.readString();
        this.siteUrl = in.readString();
        this.resIconId = in.readInt();
        int tmpTypeMenu = in.readInt();
        this.typeMenu = tmpTypeMenu == -1 ? null : TypeMenu.values()[tmpTypeMenu];
    }

    public static final Creator<DigitalCategoryItemHeader> CREATOR =
            new Creator<DigitalCategoryItemHeader>() {
                @Override
                public DigitalCategoryItemHeader createFromParcel(Parcel source) {
                    return new DigitalCategoryItemHeader(source);
                }

                @Override
                public DigitalCategoryItemHeader[] newArray(int size) {
                    return new DigitalCategoryItemHeader[size];
                }
            };
}
