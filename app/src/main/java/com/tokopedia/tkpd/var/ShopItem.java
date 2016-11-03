package com.tokopedia.tkpd.var;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by Nisie on 10/06/15.
 * add parceler support by m.normansyah 31/10/2015
 */
@Parcel
public class ShopItem extends RecyclerViewItem{


    @SerializedName("shop_name")// 1
    public String name;
    @SerializedName("shop_image")// 2
    public String iconUri;

    public String coverUri;// 3

    @SerializedName("shop_location")// 4
    public String location;

    public String isFav;// 5
    @SerializedName("shop_id")
    public String id;// 6
    @SerializedName("ad_key")
    public String adKey;// 7
    @SerializedName("ad_r")
    public String adR;// 8
    @SerializedName("shop_click_url")
    public String shopClickUrl;

    public ShopItem() {

    }
}