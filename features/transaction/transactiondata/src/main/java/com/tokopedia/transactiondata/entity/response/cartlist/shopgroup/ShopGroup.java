package com.tokopedia.transactiondata.entity.response.cartlist.shopgroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transactiondata.entity.response.cartlist.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

public class ShopGroup {

    @SerializedName("user_address_id")
    @Expose
    private int userAddressId;
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("sort_key")
    @Expose
    private int sortKey;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("cart_string")
    @Expose
    private String cartString;
    @SerializedName("cart_details")
    @Expose
    private List<CartDetail> cartDetails = new ArrayList<>();
    @SerializedName("has_promo_list")
    @Expose
    private Boolean hasPromoList;

    public int getUserAddressId() {
        return userAddressId;
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getSortKey() {
        return sortKey;
    }

    public Shop getShop() {
        return shop;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public String getCartString() { return cartString; }

    public Boolean getHasPromoList() { return hasPromoList; }
}
