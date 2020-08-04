
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Get shop info object from tkpd shop
 */
@Deprecated
public class ShopModel {

    @SerializedName("info")
    @Expose
    public Info info;
    @SerializedName("stats")
    @Expose
    public Stats stats;

    public Info getInfo() {
        return info;
    }

}
