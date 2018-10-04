package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductTypeFactoryAdapter;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopSeeMoreModelContent implements Visitable<ReviewShopInfoTypeFactoryAdapter> {
    @Override
    public int type(ReviewShopInfoTypeFactoryAdapter typeFactory) {
        return typeFactory.type(this);
    }
}
