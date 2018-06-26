package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;

public class ShipmentCheckoutButtonViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_CHECKOUT_BUTTON = R.layout.item_shipment_checkout_button;

    private TextView tvTotalPayment;
    private TextView tvSelectPaymentMethod;

    private ShipmentAdapterActionListener actionListener;

    public ShipmentCheckoutButtonViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;

        tvTotalPayment = itemView.findViewById(R.id.tv_total_payment);
        tvSelectPaymentMethod = itemView.findViewById(R.id.tv_select_payment_method);
    }

    public void bindViewHolder(ShipmentCheckoutButtonModel shipmentCheckoutButtonModel) {
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
