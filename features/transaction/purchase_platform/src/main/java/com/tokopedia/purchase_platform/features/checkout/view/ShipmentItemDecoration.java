package com.tokopedia.purchase_platform.features.checkout.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder;
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShipmentButtonPaymentViewHolder;
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShipmentDonationViewHolder;
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShipmentEmasViewHolder;
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShipmentNotifierViewHolder;

/**
 * @author anggaprasetiyo on 06/02/18.
 */

public class ShipmentItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpaceHeight;
    private Context context;

    public ShipmentItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (context == null) {
            context = parent.getContext();
            verticalSpaceHeight = (int) context.getResources().getDimension(R.dimen.dp_8);
        }
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (viewHolder instanceof ShipmentNotifierViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else if (viewHolder instanceof PromoGlobalViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_8);
        } else if (viewHolder instanceof ShipmentDonationViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else if (viewHolder instanceof ShipmentEmasViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
            outRect.top = (int) context.getResources().getDimension(R.dimen.dp_8);
        } else if (viewHolder instanceof ShipmentButtonPaymentViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_0);
        } else if (viewHolder.getAdapterPosition() == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = (int) context.getResources().getDimension(R.dimen.dp_14);
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
