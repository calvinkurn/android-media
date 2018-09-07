package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;

/**
 * @author Irfan Khoirul on 14/05/18.
 */

public class ShipmentCheckoutButtonViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_CHECKOUT_BUTTON = R.layout.item_shipment_checkout_button;

    private LinearLayout llContainer;
    private TextView tvTotalPayment;
    private TextView tvSelectPaymentMethod;

    private ShipmentAdapterActionListener actionListener;

    public ShipmentCheckoutButtonViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;

        llContainer = itemView.findViewById(R.id.ll_container);
        tvTotalPayment = itemView.findViewById(R.id.tv_total_payment);
        tvSelectPaymentMethod = itemView.findViewById(R.id.tv_select_payment_method);
    }

    public void bindViewHolder(ShipmentCheckoutButtonModel shipmentCheckoutButtonModel) {
        if (shipmentCheckoutButtonModel.isHideBottomShadow()) {
            llContainer.setBackgroundResource(R.drawable.bg_shadow_top);
        } else {
            llContainer.setBackgroundResource(R.drawable.bg_shadow);
        }
        tvSelectPaymentMethod.setBackgroundResource(R.drawable.bg_button_orange_enabled);
        tvSelectPaymentMethod.setTextColor(tvSelectPaymentMethod.getContext().getResources().getColor(R.color.white));
        tvSelectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onChoosePaymentMethodButtonClicked(shipmentCheckoutButtonModel.isAbleToCheckout());
            }
        });

        if (!TextUtils.isEmpty(shipmentCheckoutButtonModel.getTotalPayment())) {
            tvTotalPayment.setText(shipmentCheckoutButtonModel.getTotalPayment());
        } else {
            tvTotalPayment.setText("-");
        }
    }


}
