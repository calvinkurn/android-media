package com.tokopedia.tkpd.home.wishlist.domain.model;

/**
 * @author Kulomady on 2/20/17.
 */
public class WholesalePriceDomain {

    private int minimum;
    private int maximum;
    private int price;

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
