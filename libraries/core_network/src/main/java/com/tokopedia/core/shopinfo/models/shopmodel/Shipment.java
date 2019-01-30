
package com.tokopedia.core.shopinfo.models.shopmodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Shipment {

    @SerializedName("shipping_max_add_fee")
    @Expose
    public int shippingMaxAddFee;
    @SerializedName("shipment_id")
    @Expose
    public String shipmentId;
    @SerializedName("shipment_package")
    @Expose
    public List<ShipmentPackage> shipmentPackage = new ArrayList<ShipmentPackage>();
    @SerializedName("shipment_available")
    @Expose
    public int shipmentAvailable;
    @SerializedName("shipment_image")
    @Expose
    public String shipmentImage;
    @SerializedName("shipment_name")
    @Expose
    public String shipmentName;

}
