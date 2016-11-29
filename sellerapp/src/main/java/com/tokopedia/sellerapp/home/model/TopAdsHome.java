package com.tokopedia.sellerapp.home.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by noiz354 on 3/2/16.
 */
public class TopAdsHome {
    @SerializedName("data")
     Data[] data;

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public static class ImageShop {
        @SerializedName("cover")
         String cover;

        // This is very small size of photo profile.
        @SerializedName("xs_url")
         String xsUrl;

        @SerializedName("s_url")
         String sUrl;
    }

    public static class Shop {
        @SerializedName("id")
         String id;

        @SerializedName("location")
         String location;

        @SerializedName("gold_shop")
         String goldShop;

        // this is 5 or lower images product, not used this time.
//         String image_product;

        @SerializedName("name")
         String name;

        @SerializedName("uri")
         String uri;

        @SerializedName("image_shop")
         ImageShop imageShop;

        @SerializedName("lucky_shop")
         String luckyShop;
    }

    public static class Data {
        @SerializedName("id")
         String id;

        @SerializedName("shop")
         Shop shop;

        @SerializedName("redirect")
         String redirect;

        @SerializedName("ad_ref_key")
         String adRefKey;

        @SerializedName("shop_click_url")
         String shopClickUrl;
    }
}
