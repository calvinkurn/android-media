package com.tokopedia.purchase_platform.features.cart.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecentViewViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecommendationViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartSectionHeaderViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartWishlistViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.viewholder.TickerAnnouncementViewHolder;

/**
 * @author anggaprasetiyo on 06/02/18.
 */

public class CartItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpaceHeight;
    private Context context;

    public CartItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (context == null) {
            context = parent.getContext();
            verticalSpaceHeight = (int) context.getResources().getDimension(R.dimen.dp_8);
        }
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (viewHolder instanceof CartPromoSuggestionViewHolder) {
            CartPromoSuggestionHolderData cartPromoSuggestionHolderData =
                    ((CartPromoSuggestionViewHolder) viewHolder).getCartPromoSuggestionHolderData();
            if (cartPromoSuggestionHolderData.isVisible()) {
                outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_8);
                outRect.left = (int) context.getResources().getDimension(R.dimen.dp_16);
                outRect.right = (int) context.getResources().getDimension(R.dimen.dp_16);
            } else {
                outRect.bottom = 0;
            }
        } else if (viewHolder instanceof PromoGlobalViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_8);
        } else if (viewHolder instanceof CartTickerErrorViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_6);
        } else if (viewHolder.getAdapterPosition() == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_14);
        } else if (viewHolder instanceof CartRecentViewViewHolder ||
                viewHolder instanceof CartWishlistViewHolder ||
                viewHolder instanceof CartRecommendationViewHolder ||
                viewHolder instanceof CartSectionHeaderViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else if (viewHolder instanceof TickerAnnouncementViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
