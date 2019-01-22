package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.SerializedName;

/**
 * Created by noiz354 on 3/2/16.
 */

@Deprecated
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
        public String cover;

        @SerializedName("cover_ecs")
        public String coverEcs;

        // This is very small size of photo profile.
        @SerializedName("xs_ecs")
        public String xsUrl;

        @SerializedName("s_ecs")
        public String sUrl;
    }

    public static class Shop {
        @SerializedName("id")
        public
        String id;

        @SerializedName("location")
        public String location;

        @SerializedName("gold_shop")
        public String goldShop;

        // this is 5 or lower images product, not used this time.
//         String image_product;

        @SerializedName("name")
        public String name;

        @SerializedName("uri")
        public String uri;

        @SerializedName("image_shop")
        public ImageShop imageShop;

        @SerializedName("lucky_shop")
        public String luckyShop;

        @SerializedName("shop_is_official")
        public boolean shopIsOfficial;

        @SerializedName("domain")
        public String domain;
    }

    public static class Data {
        @SerializedName("id")
        public String id;

        @SerializedName("shop")
        public Shop shop;

        @SerializedName("redirect")
        public String redirect;

        @SerializedName("ad_ref_key")
        public String adRefKey;

        @SerializedName("shop_click_url")
        public String shopClickUrl;

        public boolean isSelected;
    }
}
