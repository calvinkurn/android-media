package com.tokopedia.core.product.model.goldmerchant;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryPri on 16/06/17.
 */

@Deprecated
public class FeaturedProductServiceModel {
    @SerializedName("data")
    List<FeaturedProductItem> itemList = new ArrayList<>();

    public List<FeaturedProductItem> getItemList() {
        return itemList;
    }
}
