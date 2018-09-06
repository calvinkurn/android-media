package com.tokopedia.digital_deals.view.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_thumbnail")
    @Expose
    private int isThumbnail;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("status")
    @Expose
    private int status;

    public Media(){

    }

    protected Media(Parcel in) {
        id = in.readInt();
        productId = in.readInt();
        title = in.readString();
        isThumbnail = in.readInt();
        type = in.readString();
        description = in.readString();
        url = in.readString();
        client = in.readString();
        status = in.readInt();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(int isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(productId);
        dest.writeString(title);
        dest.writeInt(isThumbnail);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(client);
        dest.writeInt(status);
    }
}