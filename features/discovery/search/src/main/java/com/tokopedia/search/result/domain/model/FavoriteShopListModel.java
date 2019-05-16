package com.tokopedia.search.result.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FavoriteShopListModel {

    @SerializedName("data")
    @Expose
    public List<String> favoriteShopList = new ArrayList<>();
}
