package com.tokopedia.checkout.view.view.cartlist;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.viewholder.CartPromoSuggestionViewHolder;

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
            verticalSpaceHeight = (int) context.getResources().getDimension(R.dimen.dp_0);
        }
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (viewHolder instanceof CartPromoSuggestionViewHolder) {
            CartPromoSuggestion cartPromoSuggestion =
                    ((CartPromoSuggestionViewHolder) viewHolder).getCartPromoSuggestion();
            if (cartPromoSuggestion.isVisible()) {
                outRect.bottom = verticalSpaceHeight;
                outRect.left = verticalSpaceHeight / 2;
                outRect.right = verticalSpaceHeight / 2;
            } else {
                outRect.bottom = 0;
            }
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
