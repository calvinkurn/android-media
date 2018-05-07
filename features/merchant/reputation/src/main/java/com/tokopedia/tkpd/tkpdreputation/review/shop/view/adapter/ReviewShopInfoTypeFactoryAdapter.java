package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductContentViewHolder;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoTypeFactoryAdapter extends ReviewShopTypeFactoryAdapter {
    private ReviewShopSeeMoreViewHolder.ShopReviewSeeMoreHolderListener reviewSeeMoreHolderListener;

    public ReviewShopInfoTypeFactoryAdapter(ReviewProductContentViewHolder.ListenerReviewHolder viewListener,
                                            ReviewShopViewHolder.ShopReviewHolderListener shopReviewHolderListener,
                                            ReviewShopSeeMoreViewHolder.ShopReviewSeeMoreHolderListener reviewSeeMoreHolderListener) {
        super(viewListener, shopReviewHolderListener);
        this.reviewSeeMoreHolderListener = reviewSeeMoreHolderListener;
    }

    public int type(ReviewShopSeeMoreModelContent reviewShopSeeMoreModelContent) {
        return ReviewShopSeeMoreViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ReviewShopSeeMoreViewHolder.LAYOUT){
            return new ReviewShopSeeMoreViewHolder(parent, reviewSeeMoreHolderListener);
        }else{
            return super.createViewHolder(parent, type);
        }
    }
}
