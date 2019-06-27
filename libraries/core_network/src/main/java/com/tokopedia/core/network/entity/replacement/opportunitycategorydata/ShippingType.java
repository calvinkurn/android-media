
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ShippingType {

    @SerializedName("shippingTypeID")
    @Expose
    private int shippingTypeID;
    @SerializedName("shippingTypeName")
    @Expose
    private String shippingTypeName;

    public int getShippingTypeID() {
        return shippingTypeID;
    }

    public void setShippingTypeID(int shippingTypeID) {
        this.shippingTypeID = shippingTypeID;
    }

    public String getShippingTypeName() {
        return shippingTypeName;
    }

    public void setShippingTypeName(String shippingTypeName) {
        this.shippingTypeName = shippingTypeName;
    }

}
