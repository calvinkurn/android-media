package com.tokopedia.tkpd.home.feed.domain.model;

/**
 * @author Kulomady on 12/8/16.
 */

public class WholesalePrice {

    private String quantityMinFormat;
    private String quantityMaxFormat;
    private String priceFormat;

    public String getQuantityMinFormat() {
        return quantityMinFormat;
    }

    public void setQuantityMinFormat(String quantityMinFormat) {
        this.quantityMinFormat = quantityMinFormat;
    }

    public String getQuantityMaxFormat() {
        return quantityMaxFormat;
    }

    public void setQuantityMaxFormat(String quantityMaxFormat) {
        this.quantityMaxFormat = quantityMaxFormat;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }
}
