
package com.tokopedia.sellerapp.home.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ShopBadgeLevel {

    @SerializedName("level")
    @Expose
    public int level;
    @SerializedName("set")
    @Expose
    public int set;

}
