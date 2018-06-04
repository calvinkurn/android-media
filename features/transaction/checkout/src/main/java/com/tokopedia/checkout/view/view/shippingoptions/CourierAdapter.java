package com.tokopedia.checkout.view.view.shippingoptions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.view.view.shippingoptions.viewholder.CourierHeaderViewHolder;
import com.tokopedia.checkout.view.view.shippingoptions.viewholder.CourierTickerViewHolder;
import com.tokopedia.checkout.view.view.shippingoptions.viewholder.CourierViewHolder;
import com.tokopedia.checkout.view.view.shippingoptions.viewmodel.ShipmentOptionData;
import com.tokopedia.checkout.view.view.shippingoptions.viewmodel.ShipmentTypeData;
import com.tokopedia.checkout.view.view.shippingoptions.viewmodel.ShipmentTickerInfoData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class CourierAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_COURIER = R.layout.holder_item_courier;
    private static final int ITEM_VIEW_SHIPMENT_TYPE = R.layout.item_shipment_category;
    private static final int ITEM_VIEW_SHIPMENT_TICKER = R.layout.item_courier_ticker;

    private CourierAdapterActionListener actionListener;
    private List<ShipmentOptionData> shipmentDataList;

    public void setViewListener(CourierAdapterActionListener viewListener) {
        this.actionListener = viewListener;
    }

    public void setShipmentDataList(List<ShipmentOptionData> shipmentDataList) {
        this.shipmentDataList = shipmentDataList;
    }

    public List<ShipmentOptionData> getShipmentDataList() {
        return shipmentDataList;
    }

    public CourierAdapterActionListener getActionListener() {
        return actionListener;
    }

    @Override
    public int getItemViewType(int position) {
        ShipmentOptionData shipmentData = shipmentDataList.get(position);

        if (shipmentData instanceof ShipmentTickerInfoData) {
            return ITEM_VIEW_SHIPMENT_TICKER;
        } else if (shipmentData instanceof CourierItemData) {
            return ITEM_VIEW_COURIER;
        } else if (shipmentData instanceof ShipmentTypeData) {
            return ITEM_VIEW_SHIPMENT_TYPE;
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == ITEM_VIEW_SHIPMENT_TICKER) {
            return new CourierTickerViewHolder(view);
        } else if (viewType == ITEM_VIEW_COURIER) {
            return new CourierViewHolder(view);
        } else if (viewType == ITEM_VIEW_SHIPMENT_TYPE) {
            return new CourierHeaderViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        ShipmentOptionData shipmentData = shipmentDataList.get(position);

        if (viewType == ITEM_VIEW_SHIPMENT_TICKER) {
            ((CourierTickerViewHolder) holder).bindData((ShipmentTickerInfoData) shipmentData);
        } else if (viewType == ITEM_VIEW_COURIER) {
            ((CourierViewHolder) holder).bindData(this, (CourierItemData) shipmentData);
        } else if (viewType == ITEM_VIEW_SHIPMENT_TYPE) {
            ((CourierHeaderViewHolder) holder).bindData((ShipmentTypeData) shipmentData);
        }

    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

}
