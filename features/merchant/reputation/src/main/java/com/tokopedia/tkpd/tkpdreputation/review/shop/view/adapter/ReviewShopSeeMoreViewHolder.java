package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopSeeMoreViewHolder extends AbstractViewHolder<ReviewShopSeeMoreModelContent> {
    public static final int LAYOUT = R.layout.item_shop_review_see_more;

    public ReviewShopSeeMoreViewHolder(View itemView, final ShopReviewSeeMoreHolderListener shopReviewSeeMoreHolderListener) {
        super(itemView);
        Button showCompleteReviewButton = itemView.findViewById(R.id.button_show_complete_review);
        showCompleteReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopReviewSeeMoreHolderListener.onGoToMoreReview();
            }
        });
    }

    @Override
    public void bind(ReviewShopSeeMoreModelContent reviewShopSeeMoreModelContent) {


    }

    public interface ShopReviewSeeMoreHolderListener{
        void onGoToMoreReview();
    }
}
