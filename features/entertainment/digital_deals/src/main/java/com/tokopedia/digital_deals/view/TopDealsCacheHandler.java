package com.tokopedia.digital_deals.view;

import com.tokopedia.digital_deals.view.model.ProductItem;

import java.util.List;

public class TopDealsCacheHandler {
    private static TopDealsCacheHandler singleInstance;
    private List<ProductItem> topItems;

    synchronized public static TopDealsCacheHandler init() {
        if (singleInstance == null)
            singleInstance = new TopDealsCacheHandler();
        return singleInstance;
    }

    private TopDealsCacheHandler() {
    }

    public List<ProductItem> getTopDeals() {
        return topItems;
    }

    public void setTopDeals(List<ProductItem> topItems) {
        this.topItems = topItems;
    }

    public static void deInit() {
        singleInstance = null;
    }

}
