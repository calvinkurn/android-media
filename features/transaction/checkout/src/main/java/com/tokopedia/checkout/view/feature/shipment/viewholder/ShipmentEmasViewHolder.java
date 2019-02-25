package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.shipping_recommendation.domain.shipping.EgoldAttributeModel;

public class ShipmentEmasViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_EMAS = R.layout.checkout_holder_item_emas;

    private CheckBox buyEmas;
    private TextView tvEmasTitle;
    private TextView tvEmasDesc;
    private ImageView imgEmasInfo;
    private LinearLayout llContainer;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentEmasViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        buyEmas = itemView.findViewById(R.id.cb_emas);
        tvEmasTitle = itemView.findViewById(R.id.tv_emas_title);
        tvEmasDesc = itemView.findViewById(R.id.tv_emas_sub_title);
        imgEmasInfo = itemView.findViewById(R.id.img_emas_info);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    public void bindViewHolder(EgoldAttributeModel egoldAttributeModel) {
        llContainer.setOnClickListener(v -> buyEmas.setChecked(!buyEmas.isChecked()));
        buyEmas.setChecked(egoldAttributeModel.isChecked());
        tvEmasTitle.setText(egoldAttributeModel.getTitleText());
        imgEmasInfo.setOnClickListener(v -> showBottomSheet(egoldAttributeModel));

        tvEmasDesc.setText(String.format(llContainer.getContext()
                .getString(R.string.emas_checkout_desc), String.valueOf(egoldAttributeModel.getBuyEgoldValue())));

        buyEmas.setOnCheckedChangeListener((buttonView, isChecked) -> shipmentAdapterActionListener.onEgoldChecked(isChecked));
    }

    private void showBottomSheet(EgoldAttributeModel egoldAttributeModel) {
        Tooltip tooltip = new Tooltip(imgEmasInfo.getContext());
        tooltip.setTitle(egoldAttributeModel.getTooltipText());
        tooltip.setDesc(egoldAttributeModel.getTickerText());
        tooltip.setTextButton(imgEmasInfo.getContext().getString(R.string.label_button_bottomsheet_close));
        tooltip.setIcon(R.drawable.ic_donation);
        tooltip.getBtnAction().setOnClickListener(v -> tooltip.dismiss());
        tooltip.show();
    }

}
