package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand implements Parcelable {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("seo_url")
    @Expose
    private String seoUrl;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("featured_thumbnail_image")
    @Expose
    private String featuredThumbnailImage;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    public final static Parcelable.Creator<Brand> CREATOR = new Creator<Brand>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        public Brand[] newArray(int size) {
            return (new Brand[size]);
        }

    };

    protected Brand(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.seoUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.featuredImage = ((String) in.readValue((String.class.getClassLoader())));
        this.featuredThumbnailImage = ((String) in.readValue((String.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Brand() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(url);
        dest.writeValue(seoUrl);
        dest.writeValue(featuredImage);
        dest.writeValue(featuredThumbnailImage);
        dest.writeValue(cityName);
    }

    public int describeContents() {
        return 0;
    }

}