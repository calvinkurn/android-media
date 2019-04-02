package com.tokopedia.posapp.product.management.data.pojo.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okasurya on 4/9/18.
 */

public class EditProductRequest {
    @SerializedName("shop_id")
    @Expose
    private long shopId;
    @SerializedName("outlet_id")
    @Expose
    private long outletId;
    @SerializedName("product_price")
    @Expose
    private List<ProductPriceRequest> productPrice;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getOutletId() {
        return outletId;
    }

    public void setOutletId(long outletId) {
        this.outletId = outletId;
    }

    public List<ProductPriceRequest> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<ProductPriceRequest> productPrice) {
        this.productPrice = productPrice;
    }
}
