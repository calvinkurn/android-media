package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductTypeFactoryAdapter;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopTypeFactoryAdapter extends ReviewProductTypeFactoryAdapter {

    private ReviewShopViewHolder.ShopReviewHolderListener shopReviewHolderListener;

    public ReviewShopTypeFactoryAdapter(ReviewProductContentViewHolder.ListenerReviewHolder viewListener,
                                        ReviewShopViewHolder.ShopReviewHolderListener shopReviewHolderListener) {
        super(viewListener);
        this.shopReviewHolderListener = shopReviewHolderListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ReviewShopViewHolder.LAYOUT){
            return new ReviewShopViewHolder(parent, viewListener, shopReviewHolderListener);
        }else{
            return super.createViewHolder(parent, type);
        }
    }

    public int type(ReviewShopModelContent shopReviewModelContent) {
        return ReviewShopViewHolder.LAYOUT;
    }
}
