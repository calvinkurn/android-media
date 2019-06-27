package com.tokopedia.checkout.view.feature.shipment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentCartItemViewHolder;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;

import java.util.List;

/**
 * @author Aghny A. Putra on 05/02/18
 */

public class ShipmentInnerProductListAdapter extends RecyclerView.Adapter<ShipmentCartItemViewHolder> {

    private List<CartItemModel> mCartItemList;
    private ShipmentCartItemViewHolder.ShipmentItemListener mListener;

    public ShipmentInnerProductListAdapter(List<CartItemModel> cartItemList, ShipmentCartItemViewHolder.ShipmentItemListener listener) {
        mCartItemList = cartItemList;
        mListener = listener;
    }

    @Override
    public ShipmentCartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_product, parent, false);
        return new ShipmentCartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShipmentCartItemViewHolder holder, int position) {
        CartItemModel cartItemModel = mCartItemList.get(position);
        holder.bindViewHolder(cartItemModel, mListener);
    }

    @Override
    public int getItemCount() {
        return mCartItemList.size();
    }

}
