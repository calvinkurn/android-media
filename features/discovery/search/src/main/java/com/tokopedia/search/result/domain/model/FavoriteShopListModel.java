package com.tokopedia.search.result.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FavoriteShopListModel {

    @SerializedName("data")
    @Expose
    private List<String> favoriteShopList = new ArrayList<>();

    public void setFavoriteShopList(List<String> favoriteShopList) {
        this.favoriteShopList = favoriteShopList;
    }

    public List<String> getFavoriteShopList() {
        return favoriteShopList;
    }
}
