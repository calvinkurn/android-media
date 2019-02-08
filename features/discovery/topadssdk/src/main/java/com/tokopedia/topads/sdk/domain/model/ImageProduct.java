package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ImageProduct extends ImageHolder implements Parcelable {

    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_IMAGE_CLICK_URL = "image_click_url";

    @SerializedName(KEY_PRODUCT_ID)
    private String productId;
    @SerializedName(KEY_PRODUCT_NAME)
    private String productName;
    @SerializedName(KEY_IMAGE_URL)
    private String imageUrl;
    @SerializedName(KEY_IMAGE_CLICK_URL)
    private String imageClickUrl;

    public ImageProduct(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_PRODUCT_ID)) {
            setProductId(object.getString(KEY_PRODUCT_ID));
        }
        if(!object.isNull(KEY_PRODUCT_NAME)) {
            setProductName(object.getString(KEY_PRODUCT_NAME));
        }
        if(!object.isNull(KEY_IMAGE_URL)) {
            setImageUrl(object.getString(KEY_IMAGE_URL));
        }
        if(!object.isNull(KEY_IMAGE_CLICK_URL)) {
            setImageClickUrl(object.getString(KEY_IMAGE_CLICK_URL));
        }
    }

    protected ImageProduct(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        imageUrl = in.readString();
        imageClickUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(imageUrl);
        dest.writeString(imageClickUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageProduct> CREATOR = new Creator<ImageProduct>() {
        @Override
        public ImageProduct createFromParcel(Parcel in) {
            return new ImageProduct(in);
        }

        @Override
        public ImageProduct[] newArray(int size) {
            return new ImageProduct[size];
        }
    };

    public String getImageClickUrl() {
        return imageClickUrl;
    }

    public void setImageClickUrl(String imageClickUrl) {
        this.imageClickUrl = imageClickUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
