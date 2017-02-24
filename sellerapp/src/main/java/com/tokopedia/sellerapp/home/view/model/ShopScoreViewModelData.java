package com.tokopedia.sellerapp.home.view.model;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreViewModelData {
    private String title;
    private int value;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
