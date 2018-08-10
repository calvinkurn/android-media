package com.tokopedia.topads.sdk.domain.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ImageProduct {

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
