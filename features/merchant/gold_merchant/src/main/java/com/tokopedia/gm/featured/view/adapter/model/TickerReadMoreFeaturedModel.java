package com.tokopedia.gm.featured.view.adapter.model;

import com.tokopedia.base.list.seller.common.util.ItemType;

public class TickerReadMoreFeaturedModel implements ItemType {
    public static final int TYPE = 128392;
    private String title;
    private String description;
    private String readMoreString;

    public TickerReadMoreFeaturedModel(String title, String description, String readMoreString) {
        this.title = title;
        this.description = description;
        this.readMoreString = readMoreString;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReadMoreString() {
        return readMoreString;
    }

    @Override
    public int getType() {
        return TYPE;
    }

}
