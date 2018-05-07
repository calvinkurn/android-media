package com.tokopedia.shop.favourite.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.favourite.view.adapter.ShopFavouriteAdapterTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopFavouriteViewModel implements Visitable<ShopFavouriteAdapterTypeFactory> {

    private String id;
    private String name;
    private String imageUrl;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int type(ShopFavouriteAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
