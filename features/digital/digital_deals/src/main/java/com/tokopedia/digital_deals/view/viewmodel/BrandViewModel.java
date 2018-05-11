package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class BrandViewModel implements Parcelable {

    private String title;
    private String description;
    private String url;
    private String seoUrl;
    private String featuredImage;
    private String featuredThumbnailImage;
    public final static Parcelable.Creator<BrandViewModel> CREATOR = new Creator<BrandViewModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BrandViewModel createFromParcel(Parcel in) {
            return new BrandViewModel(in);
        }

        public BrandViewModel[] newArray(int size) {
            return (new BrandViewModel[size]);
        }

    };

    protected BrandViewModel(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.seoUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.featuredImage = ((String) in.readValue((String.class.getClassLoader())));
        this.featuredThumbnailImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BrandViewModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getFeaturedThumbnailImage() {
        return featuredThumbnailImage;
    }

    public void setFeaturedThumbnailImage(String featuredThumbnailImage) {
        this.featuredThumbnailImage = featuredThumbnailImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(url);
        dest.writeValue(seoUrl);
        dest.writeValue(featuredImage);
        dest.writeValue(featuredThumbnailImage);
    }

    public int describeContents() {
        return 0;
    }

}