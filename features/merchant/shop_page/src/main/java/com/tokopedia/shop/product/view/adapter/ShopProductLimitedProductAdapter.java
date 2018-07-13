package com.tokopedia.shop.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolderOld;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */
@Deprecated
public class ShopProductLimitedProductAdapter extends RecyclerView.Adapter<ShopProductViewHolderOld> {

    private final ShopProductClickedListener shopProductClickedListener;
    private List<ShopProductViewModelOld> list;

    public List<ShopProductViewModelOld> getList() {
        return list;
    }

    public void setList(List<ShopProductViewModelOld> list) {
        this.list = list;
    }

    public ShopProductLimitedProductAdapter(ShopProductClickedListener shopProductClickedListener) {
        this.shopProductClickedListener = shopProductClickedListener;
        list = new ArrayList<>();
    }

    @Override
    public ShopProductViewHolderOld onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ShopProductViewHolderOld.LAYOUT, parent, false);
        return new ShopProductViewHolderOld(view, shopProductClickedListener);
    }

    @Override
    public void onBindViewHolder(ShopProductViewHolderOld holder, int position) {
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
