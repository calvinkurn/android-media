package com.tokopedia.tkpd.analytics.model;

import java.util.Map;

/**
 * Created by herdimac on 6/24/16.
 */
public class Product extends BaseAnalyticsModel {

    private String price;
    private String type;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, String> getAttr() {
        return extraAttr;
    }

    @Override
    public void setExtraAttr(Map<String, String> extraAttr) {
        this.extraAttr = extraAttr;
    }
}
