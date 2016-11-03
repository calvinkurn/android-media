package com.tokopedia.tkpd.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogReview implements Parcelable {

    @SerializedName("review_from")
    @Expose
    private String reviewFrom;
    @SerializedName("review_description")
    @Expose
    private String reviewDescription;
    @SerializedName("review_rating")
    @Expose
    private String reviewRating;
    @SerializedName("review_url")
    @Expose
    private String reviewUrl;
    @SerializedName("review_from_url")
    @Expose
    private String reviewFromUrl;
    @SerializedName("catalog_id")
    @Expose
    private String catalogId;
    @SerializedName("review_from_image")
    @Expose
    private String reviewFromImage;

    public String getReviewFrom() {
        return reviewFrom;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public String getReviewFromUrl() {
        return reviewFromUrl;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public String getReviewFromImage() {
        return reviewFromImage;
    }

    public CatalogReview() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewFrom);
        dest.writeString(this.reviewDescription);
        dest.writeString(this.reviewRating);
        dest.writeString(this.reviewUrl);
        dest.writeString(this.reviewFromUrl);
        dest.writeString(this.catalogId);
        dest.writeString(this.reviewFromImage);
    }

    protected CatalogReview(Parcel in) {
        this.reviewFrom = in.readString();
        this.reviewDescription = in.readString();
        this.reviewRating = in.readString();
        this.reviewUrl = in.readString();
        this.reviewFromUrl = in.readString();
        this.catalogId = in.readString();
        this.reviewFromImage = in.readString();
    }

    public static final Creator<CatalogReview> CREATOR = new Creator<CatalogReview>() {
        @Override
        public CatalogReview createFromParcel(Parcel source) {
            return new CatalogReview(source);
        }

        @Override
        public CatalogReview[] newArray(int size) {
            return new CatalogReview[size];
        }
    };
}
