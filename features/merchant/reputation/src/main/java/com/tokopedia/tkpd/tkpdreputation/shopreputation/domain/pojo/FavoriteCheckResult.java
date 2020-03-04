package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCheckResult {

    @SerializedName("data")
    List<String> shopIds = new ArrayList<>();

    public List<String> getShopIds() {
        return shopIds;
    }

}
