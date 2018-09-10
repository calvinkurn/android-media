package com.tokopedia.train.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 23/07/18.
 */
public class TrainPromoAttributesViewModel implements Parcelable {

    private String description;
    private String linkUrl;
    private String imageUrl;
    private String promoCode;

    public TrainPromoAttributesViewModel(String description, String linkUrl, String imageUrl, String promoCode) {
        this.description = description;
        this.linkUrl = linkUrl;
        this.imageUrl = imageUrl;
        this.promoCode = promoCode;
    }

    protected TrainPromoAttributesViewModel(Parcel in) {
        description = in.readString();
        linkUrl = in.readString();
        imageUrl = in.readString();
        promoCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(linkUrl);
        dest.writeString(imageUrl);
        dest.writeString(promoCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainPromoAttributesViewModel> CREATOR = new Creator<TrainPromoAttributesViewModel>() {
        @Override
        public TrainPromoAttributesViewModel createFromParcel(Parcel in) {
            return new TrainPromoAttributesViewModel(in);
        }

        @Override
        public TrainPromoAttributesViewModel[] newArray(int size) {
            return new TrainPromoAttributesViewModel[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
