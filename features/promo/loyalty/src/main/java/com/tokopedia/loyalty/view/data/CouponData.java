package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class CouponData implements Parcelable {
    private int id;
    private int promoId;
    private String code;
    private String expired;
    private String title;
    private String subTitle;
    private String description;
    private String icon;
    private String imageUrl;
    private String imageUrlMobile;
    private String errorMessage;

    private CouponData(Builder builder) {
        setId(builder.id);
        setPromoId(builder.promoId);
        setCode(builder.code);
        setExpired(builder.expired);
        setTitle(builder.title);
        setSubTitle(builder.subTitle);
        setDescription(builder.description);
        setIcon(builder.icon);
        setImageUrl(builder.imageUrl);
        setImageUrlMobile(builder.imageUrlMobile);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }

    public void setImageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.promoId);
        dest.writeString(this.code);
        dest.writeString(this.expired);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.description);
        dest.writeString(this.icon);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageUrlMobile);
        dest.writeString(this.errorMessage);
    }

    public CouponData() {
    }

    protected CouponData(Parcel in) {
        this.id = in.readInt();
        this.promoId = in.readInt();
        this.code = in.readString();
        this.expired = in.readString();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.description = in.readString();
        this.icon = in.readString();
        this.imageUrl = in.readString();
        this.imageUrlMobile = in.readString();
        this.errorMessage = in.readString();
    }

    public static final Creator<CouponData> CREATOR = new Creator<CouponData>() {
        @Override
        public CouponData createFromParcel(Parcel source) {
            return new CouponData(source);
        }

        @Override
        public CouponData[] newArray(int size) {
            return new CouponData[size];
        }
    };


    public static final class Builder {
        private int id;
        private int promoId;
        private String code;
        private String expired;
        private String title;
        private String subTitle;
        private String description;
        private String icon;
        private String imageUrl;
        private String imageUrlMobile;
        private String errorMessage;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder promoId(int val) {
            promoId = val;
            return this;
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder expired(String val) {
            expired = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder subTitle(String val) {
            subTitle = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder imageUrl(String val) {
            imageUrl = val;
            return this;
        }

        public Builder imageUrlMobile(String val) {
            imageUrlMobile = val;
            return this;
        }

        public Builder errorMessage(String val) {
            errorMessage = val;
            return this;
        }

        public CouponData build() {
            return new CouponData(this);
        }
    }
}
