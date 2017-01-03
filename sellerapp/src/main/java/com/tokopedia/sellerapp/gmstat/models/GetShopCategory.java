package com.tokopedia.sellerapp.gmstat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 11/14/16.
 */
public class GetShopCategory {

    @SerializedName("ShopCategory")
    @Expose
    private List<Integer> shopCategory = new ArrayList<Integer>();

    /**
     *
     * @return
     * The shopCategory
     */
    public List<Integer> getShopCategory() {
        return shopCategory;
    }

    /**
     *
     * @param shopCategory
     * The ShopCategory
     */
    public void setShopCategory(List<Integer> shopCategory) {
        this.shopCategory = shopCategory;
    }

}
