package com.tokopedia.checkout.view.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.R2;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class CourierChoiceAdapter extends RecyclerView.Adapter<CourierChoiceAdapter.CourierViewHolder> {

    private static final String FONT_FAMILY_SANS_SERIF = "sans-serif";
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";
    private ViewListener viewListener;
    private List<CourierItemData> couriers;

    @Inject
    public CourierChoiceAdapter() {

    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void setCouriers(List<CourierItemData> couriers) {
        this.couriers = couriers;
    }

    @Override
    public CourierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_courier, parent, false);

        return new CourierViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourierViewHolder holder, int position) {
        CourierItemData courierItemData = couriers.get(position);
        holder.tvCourierName.setText(courierItemData.getName());
        holder.tvPrice.setText(holder.tvPrice.getContext().getResources().getString(
                R.string.label_shipment_type_format, CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        courierItemData.getDeliveryPrice(), true)));

        if (courierItemData.getDeliverySchedule() != null) {
            holder.tvDeliverySchedule.setText(courierItemData.getDeliverySchedule());
            holder.tvDeliverySchedule.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeliverySchedule.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(courierItemData.getEstimatedTimeDelivery())){
            holder.tvDeliveryTimeRange.setText(courierItemData.getEstimatedTimeDelivery() + "*");
            holder.tvDeliveryTimeRange.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeliveryTimeRange.setVisibility(View.GONE);
        }

        renderTypeface(holder, courierItemData);

        holder.itemView.setOnClickListener(getItemClickListener(courierItemData, position));
    }

    private void renderTypeface(CourierViewHolder holder, CourierItemData courierItemData) {
        if (courierItemData.isSelected()) {
            holder.rbSelected.setChecked(true);
            holder.tvCourierName.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            holder.tvPrice.setTypeface(Typeface.create(
                    FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            holder.tvDeliveryTimeRange.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            holder.tvDeliverySchedule.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            viewListener.onSelectedCourierItemLoaded(courierItemData);
        } else {
            holder.rbSelected.setChecked(false);
            holder.tvCourierName.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
            holder.tvPrice.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
            holder.tvDeliveryTimeRange.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
            holder.tvDeliverySchedule.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
        }
    }

    private View.OnClickListener getItemClickListener(final CourierItemData courierItemData, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourierItemData selectedCourier = null;
                for (CourierItemData courier : couriers) {
                    if (courier.getShipperProductId() == courierItemData.getShipperProductId()) {
                        if (couriers.size() > position && position >= 0) {
                            if (!courier.isSelected()) {
                                courier.setSelected(true);
                            }
                            selectedCourier = couriers.get(position);
                        }
                    } else {
                        courier.setSelected(false);
                    }
                }
                if (selectedCourier != null) {
                    viewListener.onCourierItemClick(selectedCourier);
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return couriers.size();
    }

    public interface ViewListener {
        void onCourierItemClick(CourierItemData courierItemData);

        void onSelectedCourierItemLoaded(CourierItemData courierItemData);
    }

    static class CourierViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_courier_name)
        TextView tvCourierName;
        @BindView(R2.id.tv_price)
        TextView tvPrice;
        @BindView(R2.id.tv_delivery_time_range)
        TextView tvDeliveryTimeRange;
        @BindView(R2.id.rb_selected)
        RadioButton rbSelected;
        @BindView(R2.id.tv_delivery_schedule)
        TextView tvDeliverySchedule;

        CourierViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}