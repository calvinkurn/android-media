package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ImageProduct {

    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_IMAGE_URL = "image_url";

    private String productId;
    private String productName;
    private String imageUrl;

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
