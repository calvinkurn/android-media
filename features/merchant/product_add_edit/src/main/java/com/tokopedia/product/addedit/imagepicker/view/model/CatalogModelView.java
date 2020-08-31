package com.tokopedia.product.addedit.imagepicker.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.product.addedit.imagepicker.view.adapter.CatalogAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogModelView implements Visitable<CatalogAdapterTypeFactory> {
    private String imageUrl;

    @Override
    public int type(CatalogAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
