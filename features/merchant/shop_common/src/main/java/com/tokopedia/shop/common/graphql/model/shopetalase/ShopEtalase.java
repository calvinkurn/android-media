package com.tokopedia.shop.common.graphql.model.shopetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopEtalase {
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("count")
    @Expose
    private int count = 0;
}
