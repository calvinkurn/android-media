package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

import java.util.Iterator;

/**
 * Created by zulfikarrahman on 1/22/18.
 */

public class ReviewProductAdapter<T extends ReviewProductModel, F extends ReviewProductTypeFactoryAdapter> extends BaseListAdapter<T, F> {
    private static final int LIKE_STATUS_ACTIVE = 1;

    public ReviewProductAdapter(F baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public ReviewProductAdapter(F baseListAdapterTypeFactory, OnAdapterInteractionListener<T> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }

    public void updateLikeStatus(int likeStatus, int totalLike, String reviewId) {
        int i = 0;
        for (Iterator<T> it = getData().iterator(); it.hasNext(); ) {
            ReviewProductModel productReviewModel = it.next();
            if(productReviewModel instanceof ReviewProductModelContent) {
                ReviewProductModelContent productReviewModelContent = (ReviewProductModelContent)productReviewModel;
                if (productReviewModelContent.getReviewId().equalsIgnoreCase(reviewId)) {
                    productReviewModelContent.setLikeStatus(likeStatus == LIKE_STATUS_ACTIVE);
                    productReviewModelContent.setTotalLike(totalLike);
                    notifyItemChanged(i);
                    return;
                }
            }
            i++;
        }
    }

    public void updateDeleteReview(String reviewId) {
        int i = 0;
        for (Iterator<T> it = getData().iterator(); it.hasNext(); ) {
            ReviewProductModel productReviewModel = it.next();
            if(productReviewModel instanceof ReviewProductModelContent) {
                ReviewProductModelContent productReviewModelContent = (ReviewProductModelContent)productReviewModel;
                if (productReviewModelContent.getReviewId().equalsIgnoreCase(reviewId)) {
                    productReviewModelContent.setReviewHasReplied(false);
                    notifyItemChanged(i);
                    return;
                }
            }
            i++;
        }
    }

    public void updateLikeStatusError(String reviewId, int likeStatus) {
        int i = 0;
        for (Iterator<T> it = getData().iterator(); it.hasNext(); ) {
            ReviewProductModel productReviewModel = it.next();
            if(productReviewModel instanceof ReviewProductModelContent) {
                ReviewProductModelContent productReviewModelContent = (ReviewProductModelContent)productReviewModel;
                if (productReviewModelContent.getReviewId().equalsIgnoreCase(reviewId)) {
                    productReviewModelContent.setLikeStatus(!(likeStatus == LIKE_STATUS_ACTIVE));
                    productReviewModelContent.setTotalLike(productReviewModelContent.totalLike > 0 ?  productReviewModelContent.totalLike - 1 : 0);
                    notifyItemChanged(i);
                    return;
                }
            }
            i++;
        }
    }
}
