
package com.tokopedia.topads.common.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ShipmentPackage {

    @SerializedName("shipping_id")
    @Expose
    public String shippingId;
    @SerializedName("product_name")
    @Expose
    public String productName;

}
