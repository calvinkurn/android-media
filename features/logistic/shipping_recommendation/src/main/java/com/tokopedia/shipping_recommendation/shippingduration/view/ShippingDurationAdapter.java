package com.tokopedia.shipping_recommendation.shippingduration.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShippingDurationViewModel> shippingDurationViewModels;
    private ShippingDurationAdapterListener shippingDurationAdapterListener;
    private int cartPosition;
    // set true if has courier promo, whether own courier or other duration's courier
    private boolean hasCourierPromo;

    public void setShippingDurationViewModels(List<ShippingDurationViewModel> shippingDurationViewModels) {
        this.shippingDurationViewModels = shippingDurationViewModels;
    }

    public void setShippingDurationAdapterListener(ShippingDurationAdapterListener shippingDurationAdapterListener) {
        this.shippingDurationAdapterListener = shippingDurationAdapterListener;
    }

    public void setCartPosition(int cartPosition) {
        this.cartPosition = cartPosition;
    }

    public void setHasCourierPromo(boolean hasCourierPromo) {
        this.hasCourierPromo = hasCourierPromo;
    }

    @Override
    public int getItemViewType(int position) {
        return ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShippingDurationViewHolder(view, this, cartPosition, hasCourierPromo);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ShippingDurationViewHolder) holder).bindData(shippingDurationViewModels.get(position),
                shippingDurationAdapterListener);
    }

    @Override
    public int getItemCount() {
        return shippingDurationViewModels.size();
    }

}
