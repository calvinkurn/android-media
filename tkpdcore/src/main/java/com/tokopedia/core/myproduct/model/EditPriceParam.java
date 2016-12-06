package com.tokopedia.core.myproduct.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 7/28/16.
 */
public class EditPriceParam {

    String productId;
    String price;
    String currency;
    String shopId;
    int position;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Map<String, String> generateEditPriceParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("product_id", getProductId());
        param.put("product_price", getPrice());
        param.put("product_price_currency", getCurrency());
        param.put("shop_id", getShopId());
        return param;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
