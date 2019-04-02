package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductTypeFactoryAdapter extends BaseAdapterTypeFactory {
    protected final ReviewProductContentViewHolder.ListenerReviewHolder viewListener;

    public ReviewProductTypeFactoryAdapter(ReviewProductContentViewHolder.ListenerReviewHolder viewListener) {
        this.viewListener = viewListener;
    }

    public int type(ReviewProductModelTitleHeader productReviewModelTitleHeader) {
        return ReviewProductTitleHeaderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ReviewProductTitleHeaderViewHolder.LAYOUT){
            return new ReviewProductTitleHeaderViewHolder(parent);
        }else if(type == ReviewProductContentViewHolder.LAYOUT) {
            return new ReviewProductContentViewHolder(parent, viewListener);
        }else{
            return super.createViewHolder(parent, type);
        }
    }

    public int type(ReviewProductModelContent productReviewModelContent) {
        return ReviewProductContentViewHolder.LAYOUT;
    }
}
