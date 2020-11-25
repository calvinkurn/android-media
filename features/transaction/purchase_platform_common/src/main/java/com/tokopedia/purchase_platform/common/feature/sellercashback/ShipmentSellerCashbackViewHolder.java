package com.tokopedia.purchase_platform.common.feature.sellercashback;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.purchase_platform.common.R;

/**
 * @author Irfan Khoirul on 12/07/18.
 */

public class ShipmentSellerCashbackViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SELLER_CASHBACK = R.layout.item_seller_cashback;

    private final SellerCashbackListener sellerCashbackListener;

    private TextView tvSellerCashbackPrice;
    private RelativeLayout rlContainer;

    public ShipmentSellerCashbackViewHolder(View itemView, SellerCashbackListener listener) {
        super(itemView);

        sellerCashbackListener = listener;
        tvSellerCashbackPrice = itemView.findViewById(R.id.tv_seller_cashback_price);
        rlContainer = itemView.findViewById(R.id.rl_container);
    }

    public void bindViewHolder(ShipmentSellerCashbackModel shipmentSellerCashbackModel) {
        if (shipmentSellerCashbackModel.isVisible()) {
            tvSellerCashbackPrice.setText(shipmentSellerCashbackModel.getSellerCashbackFmt());
            rlContainer.setVisibility(View.VISIBLE);
        } else {
            rlContainer.setVisibility(View.GONE);
        }

        sellerCashbackListener.onCashbackUpdated(shipmentSellerCashbackModel.getSellerCashback());
    }

}
