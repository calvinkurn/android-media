package com.tokopedia.checkout.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentCostViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentDonationViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentEmasViewHolder;
import com.tokopedia.checkout.view.viewholder.ShipmentInsuranceTncViewHolder;
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShippingCompletionTickerViewHolder;

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
            verticalSpaceHeight = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_8);
        }
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (viewHolder instanceof ShipmentDonationViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_8);
        } else if (viewHolder instanceof PromoCheckoutViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_8);
        } else if (viewHolder instanceof ShipmentEmasViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_8);
        } else if (viewHolder instanceof ShipmentButtonPaymentViewHolder) {
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_0);
        } else if (parent.getAdapter() != null && viewHolder.getAdapterPosition() == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_14);
        } else if (viewHolder instanceof ShipmentCostViewHolder) {
            if (parent.getAdapter() != null && viewHolder.getAdapterPosition() == parent.getAdapter().getItemCount() - 2) {
                outRect.bottom = verticalSpaceHeight;
            } else {
                outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_0);
            }
        } else if (viewHolder instanceof ShipmentInsuranceTncViewHolder) {
            if (parent.getAdapter() != null && viewHolder.getAdapterPosition() == parent.getAdapter().getItemCount() - 2) {
                outRect.top = verticalSpaceHeight;
            } else {
                outRect.top = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_0);
            }
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_0);
        } else if (viewHolder instanceof ShippingCompletionTickerViewHolder) {
            outRect.top = verticalSpaceHeight;
            outRect.bottom = (int) context.getResources().getDimension(com.tokopedia.abstraction.R.dimen.dp_0);
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
