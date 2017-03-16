package com.tokopedia.core.network.entity.wishlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class WholesalePrice {

    @SerializedName("minimum")
    int Minimum;
    @SerializedName("maximum")
    int Maximum;
    @SerializedName("price")
    int Price;

    public int getMinimum() {
        return Minimum;
    }

    public void setMinimum(int minimum) {
        Minimum = minimum;
    }

    public int getMaximum() {
        return Maximum;
    }

    public void setMaximum(int maximum) {
        Maximum = maximum;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
