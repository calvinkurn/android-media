
package com.tokopedia.core.shopinfo.models.shopmodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Get shop info object from tkpd shop
 */
@Deprecated
public class ShopModel {

    public static final int IS_OPEN = 1; //FROM API
    public static final int IS_CLOSED = 2; //FROM API
    public static final int IS_SCHEDULED_CLOSED = 3; //FROM API

    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("info")
    @Expose
    public Info info;
    @SerializedName("payment")
    @Expose
    public List<Payment> payment = new ArrayList<Payment>();
    @SerializedName("stats")
    @Expose
    public Stats stats;
    @SerializedName("shop_tx_stats")
    @Expose
    public ShopTxStats shopTxStats;
    @SerializedName("closed_info")
    @Expose
    public ClosedInfo closedInfo;
    @SerializedName("shipment")
    @Expose
    public List<Shipment> shipment = new ArrayList<Shipment>();
    @SerializedName("ratings")
    @Expose
    public Ratings ratings;
    @SerializedName("is_open")
    @Expose
    public int isOpen;
    @SerializedName("address")
    @Expose
    public List<Address> address = new ArrayList<Address>();
    @SerializedName("use_ace")
    @Expose
    public int useAce;

    public Owner getOwner() {
        return owner;
    }

    public Info getInfo() {
        return info;
    }

    public List<Payment> getPayment() {
        return payment;
    }

    public Stats getStats() {
        return stats;
    }

    public ShopTxStats getShopTxStats() {
        return shopTxStats;
    }

    public ClosedInfo getClosedInfo() {
        return closedInfo;
    }

    public List<Shipment> getShipment() {
        return shipment;
    }

    public Ratings getRatings() {
        return ratings;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public List<Address> getAddress() {
        return address;
    }

    public int getUseAce() {
        return useAce;
    }
}
