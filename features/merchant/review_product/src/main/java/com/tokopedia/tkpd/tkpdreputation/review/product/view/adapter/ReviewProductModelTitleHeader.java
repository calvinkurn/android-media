package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductModelTitleHeader implements ReviewProductModel {
    private String title;

    public ReviewProductModelTitleHeader(String title) {
        this.title = title;
    }

    @Override
    public int type(ReviewProductTypeFactoryAdapter typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }
}
