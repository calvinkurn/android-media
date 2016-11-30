
package com.tokopedia.sellerapp.home.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ShipmentPackage {

    @SerializedName("shipping_id")
    @Expose
    public String shippingId;
    @SerializedName("product_name")
    @Expose
    public String productName;

}
