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
    private Shop shop = new Shop();
    @SerializedName("is_fulfillment_service")
    @Expose
    private boolean isFulFillment;
    @SerializedName("warehouse")
    @Expose
    private Warehouse warehouse;
    @SerializedName("cart_string")
    @Expose
    private String cartString = "";
    @SerializedName("cart_details")
    @Expose
    private List<CartDetail> cartDetails = new ArrayList<>();
    @SerializedName("has_promo_list")
    @Expose
    private boolean hasPromoList;
    @SerializedName("checkbox_state")
    @Expose
    private boolean checkboxState;
    @SerializedName("free_shipping")
    private FreeShipping freeShipping;

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

    public boolean isFulFillment() {
        return isFulFillment;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public String getCartString() { return cartString; }

    public boolean getHasPromoList() { return hasPromoList; }

    public boolean isCheckboxState() {
        return checkboxState;
    }

    public FreeShipping getFreeShipping() {
        return freeShipping;
    }
}
