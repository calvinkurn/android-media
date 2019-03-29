package com.tokopedia.core.product.model.productdink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/03/2016.
 */

@Deprecated
public class ProductDinkData {
    private static final String TAG = ProductDinkData.class.getSimpleName();

    @SerializedName("is_dink")
    @Expose
    private int isDink;
    @SerializedName("p_name_enc")
    @Expose
    private String productName;
    @SerializedName("dink_time_expiry")
    @Expose
    private String expiry;
    @SerializedName("p_uri")
    @Expose
    private String productUri;

    public int getIsDink() {
        return isDink;
    }

    public void setIsDink(int isDink) {
        this.isDink = isDink;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getProductUri() {
        return productUri;
    }

    public void setProductUri(String productUri) {
        this.productUri = productUri;
    }
}
