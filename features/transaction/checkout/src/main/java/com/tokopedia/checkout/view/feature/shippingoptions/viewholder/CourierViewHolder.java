package com.tokopedia.checkout.view.feature.shippingoptions.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.TypeFaceUtil;
import com.tokopedia.checkout.view.feature.shippingoptions.CourierAdapter;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentOptionData;

import java.util.List;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public class CourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_COURIER = R.layout.holder_item_courier;

    private TextView tvCourierName;
    private TextView tvPrice;
    private TextView tvDeliveryTimeRange;
    private RadioButton rbSelected;
    private TextView tvDeliverySchedule;

    public CourierViewHolder(View itemView) {
        super(itemView);

        tvCourierName = itemView.findViewById(R.id.tv_courier_name);
        tvPrice = itemView.findViewById(R.id.tv_price);
        tvDeliveryTimeRange = itemView.findViewById(R.id.tv_delivery_time_range);
        rbSelected = itemView.findViewById(R.id.rb_selected);
        tvDeliverySchedule = itemView.findViewById(R.id.tv_delivery_schedule);

    }

    public void bindData(CourierAdapter courierAdapter, CourierItemData courierItemData) {
        tvCourierName.setText(courierItemData.getName());
        if (courierItemData.getShipperPrice() != 0) {
            tvPrice.setText(tvPrice.getContext().getResources().getString(
                    R.string.label_shipment_type_format, CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            courierItemData.getShipperPrice(), true)));
        } else if (!TextUtils.isEmpty(courierItemData.getShipperFormattedPrice())) {
            tvPrice.setText(tvPrice.getContext().getResources().getString(
                    R.string.label_shipment_type_format, courierItemData.getShipperFormattedPrice()));
        } else {
            tvPrice.setVisibility(View.GONE);
        }

        if (courierItemData.getDeliverySchedule() != null) {
            tvDeliverySchedule.setText(courierItemData.getDeliverySchedule());
            tvDeliverySchedule.setVisibility(View.VISIBLE);
        } else {
            tvDeliverySchedule.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(courierItemData.getEstimatedTimeDelivery()) &&
                courierItemData.getMinEtd() != 0 && courierItemData.getMaxEtd() != 0) {
            tvDeliveryTimeRange.setText(courierItemData.getEstimatedTimeDelivery());
            tvDeliveryTimeRange.setVisibility(View.VISIBLE);
        } else {
            tvDeliveryTimeRange.setVisibility(View.GONE);
        }

        renderTypeface(courierAdapter, courierItemData);
        itemView.setOnClickListener(getItemClickListener(courierAdapter, courierItemData, getAdapterPosition()));
    }

    private View.OnClickListener getItemClickListener(final CourierAdapter courierAdapter, final CourierItemData courierItemData, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourierItemData selectedCourier = null;
                List<ShipmentOptionData> shipmentDataList = courierAdapter.getShipmentDataList();
                for (ShipmentOptionData shipmentData : shipmentDataList) {
                    if (shipmentData instanceof CourierItemData) {
                        CourierItemData courier = (CourierItemData) shipmentData;
                        if (courier.getShipperProductId() ==
                                courierItemData.getShipperProductId()) {
                            if (shipmentDataList.size() > position && position >= 0) {
                                if (!courier.isSelected()) {
                                    courier.setSelected(true);
                                }
                                selectedCourier = courier;
                            }
                        } else {
                            courier.setSelected(false);
                        }
                    }
                }
                if (selectedCourier != null) {
                    courierAdapter.getActionListener().onCourierItemClick(selectedCourier);
                }
                courierAdapter.notifyDataSetChanged();
            }
        };
    }

    private void renderTypeface(CourierAdapter courierAdapter, CourierItemData courierItemData) {
        if (courierItemData.isSelected()) {
            rbSelected.setChecked(true);
            TypeFaceUtil.setTypefaceMedium(tvCourierName);
            TypeFaceUtil.setTypefaceMedium(tvPrice);
            TypeFaceUtil.setTypefaceMedium(tvDeliveryTimeRange);
            TypeFaceUtil.setTypefaceMedium(tvDeliverySchedule);
            courierAdapter.getActionListener().onSelectedCourierItemLoaded(courierItemData);
        } else {
            rbSelected.setChecked(false);
            TypeFaceUtil.setTypefaceNormal(tvCourierName);
            TypeFaceUtil.setTypefaceNormal(tvPrice);
            TypeFaceUtil.setTypefaceNormal(tvDeliveryTimeRange);
            TypeFaceUtil.setTypefaceNormal(tvDeliverySchedule);
        }
    }

}
