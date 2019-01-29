package com.tokopedia.checkout.view.feature.cartlist;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.common.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.common.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentNotifierViewHolder;

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
            CartPromoSuggestion cartPromoSuggestion =
                    ((CartPromoSuggestionViewHolder) viewHolder).getCartPromoSuggestion();
            if (cartPromoSuggestion.isVisible()) {
                outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_8);
                outRect.left = (int) context.getResources().getDimension(R.dimen.dp_16);
                outRect.right = (int) context.getResources().getDimension(R.dimen.dp_16);
            } else {
                outRect.bottom = 0;
            }
        } else if (viewHolder instanceof ShipmentNotifierViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else if (viewHolder instanceof CartVoucherPromoViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else if (viewHolder instanceof CartTickerErrorViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_6);
        } else if (viewHolder.getAdapterPosition() == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_14);
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
