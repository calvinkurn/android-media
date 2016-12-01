package com.tokopedia.core.network.entity.wishlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class WholesalePrice {

    @SerializedName("minimum")
    int Minimum;
    @SerializedName("maximum")
    int Maximum;
    @SerializedName("price")
    int Price;
}
