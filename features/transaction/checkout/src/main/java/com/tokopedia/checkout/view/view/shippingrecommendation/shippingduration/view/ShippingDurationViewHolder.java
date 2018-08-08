package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_DURATION = R.layout.item_duration;

    private TextView tvDuration;
    private TextView tvPrice;
    private ImageView imgCheck;
    private View vSeparator;

    public ShippingDurationViewHolder(View itemView) {
        super(itemView);

        tvDuration = itemView.findViewById(R.id.tv_duration);
        tvPrice = itemView.findViewById(R.id.tv_price);
        imgCheck = itemView.findViewById(R.id.img_check);
        vSeparator = itemView.findViewById(R.id.v_separator);
    }

    public void bindData(ShippingDurationViewModel shippingDurationViewModel,
                         ShippingDurationAdapterListener shippingDurationAdapterListener) {

        tvDuration.setText(shippingDurationViewModel.getServiceData().getServiceName());
        tvPrice.setText(shippingDurationViewModel.getServiceData().getTexts().getTextRangePrice());
        imgCheck.setVisibility(shippingDurationViewModel.isSelected() ? View.VISIBLE : View.GONE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shippingDurationViewModel.setSelected(!shippingDurationViewModel.isSelected());
                shippingDurationAdapterListener.onShippingDurationChoosen(shippingDurationViewModel.getServiceData());
            }
        });

    }

}
