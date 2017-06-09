package com.tokopedia.core.product.model;

/**
 * Created by HenryPri on 12/05/17.
 */

public class CourierItem {
    private String name;
    private String info;
    private String imageUrl;

    public CourierItem(String name, String info, String imageUrl) {
        this.name = name;
        this.info = info;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
