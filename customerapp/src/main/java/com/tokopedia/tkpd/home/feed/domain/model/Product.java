package com.tokopedia.tkpd.home.feed.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 12/8/16.
 */
public class Product {
    private String id;
    private String name;
    private String price;
    private String mImgUrl;
    private List<Label> labels = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUri) {
        this.mImgUrl = imgUri;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

}
