package com.tokopedia.purchase_platform.common.feature.seller_cashback;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.checkout.view.feature.shipment.viewmodel.ShipmentSellerCashbackModel;

/**
 * @author Irfan Khoirul on 12/07/18.
 */

public class ShipmentSellerCashbackViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SELLER_CASHBACK = R.layout.item_seller_cashback;

    private TextView tvSellerCashbackPrice;
    private RelativeLayout rlContainer;

    public ShipmentSellerCashbackViewHolder(View itemView) {
        super(itemView);

        tvSellerCashbackPrice = itemView.findViewById(R.id.tv_seller_cashback_price);
        rlContainer = itemView.findViewById(R.id.rl_container);
    }

    public void bindViewHolder(ShipmentSellerCashbackModel shipmentSellerCashbackModel) {
        if (shipmentSellerCashbackModel.isVisible()) {
            tvSellerCashbackPrice.setText(shipmentSellerCashbackModel.getSellerCashback());
            rlContainer.setVisibility(View.VISIBLE);
        } else {
            rlContainer.setVisibility(View.GONE);
        }
    }

}
