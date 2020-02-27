package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.logisticcart.shipping.features.shippingduration.view.NotifierViewHolder;
import com.tokopedia.logisticcart.shipping.model.NotifierModel;
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.constant.CourierConstant;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RatesViewModelType> shippingCourierViewModels;
    private ShippingCourierAdapterListener shippingCourierAdapterListener;
    private int cartPosition;

    private boolean isCourierInstantOrSameday(int shipperId) {
        int[] ids = CourierConstant.INSTANT_SAMEDAY_COURIER;
        for (int id : ids) {
            if (shipperId == id) return true;
        }
        return false;
    }

    public void setShippingCourierViewModels(List<ShippingCourierViewModel> shippingCourierViewModels) {
        this.shippingCourierViewModels.clear();
        if (shippingCourierAdapterListener.shouldShowNotifier() && !shippingCourierViewModels.isEmpty()) {
            if (isCourierInstantOrSameday(shippingCourierViewModels.get(0).getProductData().getShipperId())) {
                this.shippingCourierViewModels.add(new NotifierModel());
            }
        }
        this.shippingCourierViewModels.addAll(shippingCourierViewModels);
    }

    public void setShippingCourierAdapterListener(ShippingCourierAdapterListener shippingCourierAdapterListener) {
        this.shippingCourierAdapterListener = shippingCourierAdapterListener;
    }

    public void setCartPosition(int cartPosition) {
        this.cartPosition = cartPosition;
    }

    @Override
    public int getItemViewType(int position) {
        if (shippingCourierViewModels.get(position) instanceof NotifierModel) {
            return NotifierViewHolder.getLAYOUT();
        }
        return ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == NotifierViewHolder.getLAYOUT()) {
            return new NotifierViewHolder(view);
        }
        return new ShippingCourierViewHolder(view, cartPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (shippingCourierViewModels.get(position) instanceof ShippingCourierViewModel) {
            ((ShippingCourierViewHolder) holder).bindData(((ShippingCourierViewModel) shippingCourierViewModels.get(position)),
                    shippingCourierAdapterListener);
        }
    }

    @Override
    public int getItemCount() {
        return shippingCourierViewModels.size();
    }

}
