package com.tokopedia.posapp.product.management.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/2/18.
 */

public class ProductPrimaryImage {
    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
