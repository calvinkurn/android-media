package com.tokopedia.shipping_recommendation.shippingcourier.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShippingCourierViewModel> shippingCourierViewModels;
    private ShippingCourierAdapterListener shippingCourierAdapterListener;
    private int cartPosition;
    private boolean hasCourierPromo;

    public void setShippingCourierViewModels(List<ShippingCourierViewModel> shippingCourierViewModels) {
        this.shippingCourierViewModels = shippingCourierViewModels;
    }

    public void setShippingCourierAdapterListener(ShippingCourierAdapterListener shippingCourierAdapterListener) {
        this.shippingCourierAdapterListener = shippingCourierAdapterListener;
    }

    public void setCartPosition(int cartPosition) {
        this.cartPosition = cartPosition;
    }

    public void setHasCourierPromo(boolean hasCourierPromo) {
        this.hasCourierPromo = hasCourierPromo;
    }

    @Override
    public int getItemViewType(int position) {
        return ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShippingCourierViewHolder(view, cartPosition, hasCourierPromo);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ShippingCourierViewHolder) holder).bindData(shippingCourierViewModels.get(position),
                shippingCourierAdapterListener);
    }

    @Override
    public int getItemCount() {
        return shippingCourierViewModels.size();
    }

}
