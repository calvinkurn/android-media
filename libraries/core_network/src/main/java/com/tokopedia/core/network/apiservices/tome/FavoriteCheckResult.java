package com.tokopedia.core.network.apiservices.tome;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryPri on 24/05/17.
 */

@Deprecated
public class FavoriteCheckResult {
    @SerializedName("data")
    List<String> shopIds = new ArrayList<>();

    public List<String> getShopIds() {
        return shopIds;
    }
}