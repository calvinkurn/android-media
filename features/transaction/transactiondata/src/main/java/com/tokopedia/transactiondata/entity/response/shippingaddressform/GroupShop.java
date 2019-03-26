package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupShop {

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("messages")
    @Expose
    private List<String> messages = new ArrayList<>();
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("shop_shipments")
    @Expose
    private List<ShopShipment> shopShipments = new ArrayList<>();
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<>();
    @SerializedName("shipping_id")
    @Expose
    private int shippingId;
    @SerializedName("sp_id")
    @Expose
    private int spId;
    @SerializedName("dropshipper")
    @Expose
    private Dropshiper dropshiper;
    @SerializedName("is_insurance")
    @Expose
    private boolean isInsurance;
    @SerializedName("is_fulfillment_service")
    @Expose
    private boolean isFulfillment;
    @SerializedName("warehouse")
    @Expose
    private Warehouse warehouse;

    public List<String> getErrors() {
        return errors;
    }

    public Shop getShop() {
        return shop;
    }

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<String> getMessages() {
        return messages;
    }

    public int getShippingId() {
        return shippingId;
    }

    public int getSpId() {
        return spId;
    }

    public Dropshiper getDropshiper() {
        return dropshiper;
    }

    public boolean isInsurance() {
        return isInsurance;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }
}
