package com.tokopedia.gallery.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.gallery.adapter.TypeFactory;

public class ImageReviewItem implements Visitable<TypeFactory>, Parcelable {
    private String formattedDate;
    private String reviewerName;
    private String imageUrlThumbnail;
    private String imageUrlLarge;
    private int rating;

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getImageUrlThumbnail() {
        return imageUrlThumbnail;
    }

    public void setImageUrlThumbnail(String imageUrlThumbnail) {
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.formattedDate);
        dest.writeString(this.reviewerName);
        dest.writeString(this.imageUrlThumbnail);
        dest.writeString(this.imageUrlLarge);
        dest.writeInt(this.rating);
    }

    public ImageReviewItem() {
    }

    protected ImageReviewItem(Parcel in) {
        this.formattedDate = in.readString();
        this.reviewerName = in.readString();
        this.imageUrlThumbnail = in.readString();
        this.imageUrlLarge = in.readString();
        this.rating = in.readInt();
    }

    public static final Creator<ImageReviewItem> CREATOR = new Creator<ImageReviewItem>() {
        @Override
        public ImageReviewItem createFromParcel(Parcel source) {
            return new ImageReviewItem(source);
        }

        @Override
        public ImageReviewItem[] newArray(int size) {
            return new ImageReviewItem[size];
        }
    };

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
