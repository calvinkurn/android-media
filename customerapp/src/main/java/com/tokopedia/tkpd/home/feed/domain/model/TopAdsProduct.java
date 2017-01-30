package com.tokopedia.tkpd.home.feed.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 12/8/16.
 */

public class TopAdsProduct extends Product {

    private String priceFormat;
    private List<WholesalePrice> wholesalePrice = new ArrayList<>();
    private String countTalkFormat;
    private String countReviewFormat;
    private String categoryId;
    private boolean preorder;
    private boolean wholesale;
    private int mShoId;

    public boolean isPreorder() {
        return preorder;
    }

    public void setPreorder(boolean preorder) {
        this.preorder = preorder;
    }


    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }

    public List<WholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getCountTalkFormat() {
        return countTalkFormat;
    }

    public void setCountTalkFormat(String countTalkFormat) {
        this.countTalkFormat = countTalkFormat;
    }

    public String getCountReviewFormat() {
        return countReviewFormat;
    }

    public void setCountReviewFormat(String countReviewFormat) {
        this.countReviewFormat = countReviewFormat;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isWholesale() {
        return wholesale;
    }

    public void setWholesale(boolean wholesale) {
        this.wholesale = wholesale;
    }

    public void setShopId(int shoId) {
        mShoId = shoId;
    }

    public int getShoId() {
        return mShoId;
    }


}
