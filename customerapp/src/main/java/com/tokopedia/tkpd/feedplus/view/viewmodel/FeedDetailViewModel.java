package com.tokopedia.tkpd.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedDetailViewModel implements Visitable<FeedPlusDetailTypeFactory> {

    private String name;
    private String price;
    private String imageSource;
    private String url;
    private String cashback;
    private boolean isWholesale;
    private boolean isPreorder;
    private boolean isFreeReturn;
    private boolean isWishlist;
    private int rating;

    public FeedDetailViewModel(String name, String imageSource) {
        this.name = name;
        this.price = "Rp 10.000";
        this.url ="http://tokopedia.com";
        this.imageSource = imageSource;
        this.cashback = "";
        this.isWholesale = false;
        this.isPreorder = false;
        this.isFreeReturn = false;
        this.isWishlist = false;
        this.rating = 4;
    }

    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        isFreeReturn = freeReturn;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public void setWishlist(boolean wishlist) {
        isWishlist = wishlist;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
