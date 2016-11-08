
package com.tokopedia.core.shopinfo.models.shopmodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopModel {

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

}
