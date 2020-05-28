package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShippingCourierUiModel> shippingCourierUiModels;
    private ShippingCourierAdapterListener shippingCourierAdapterListener;
    private int cartPosition;

    public void setShippingCourierViewModels(List<ShippingCourierUiModel> shippingCourierUiModels) {
        this.shippingCourierUiModels = shippingCourierUiModels;
    }

    public void setShippingCourierAdapterListener(ShippingCourierAdapterListener shippingCourierAdapterListener) {
        this.shippingCourierAdapterListener = shippingCourierAdapterListener;
    }

    public void setCartPosition(int cartPosition) {
        this.cartPosition = cartPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShippingCourierViewHolder(view, cartPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ShippingCourierViewHolder) holder).bindData(shippingCourierUiModels.get(position),
                shippingCourierAdapterListener, position == getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return shippingCourierUiModels.size();
    }

}
