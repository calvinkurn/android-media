package com.tokopedia.shop.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFeaturedViewHolderOld;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */
@Deprecated
public class ShopProductLimitedFeaturedAdapter extends RecyclerView.Adapter<ShopProductFeaturedViewHolderOld> {

    private final ShopProductClickedListener shopProductClickedListener;
    private ShopProductFeaturedViewHolderOld.ShopProductFeaturedListener shopProductFeaturedListener;
    private List<ShopProductViewModelOld> list;

    public List<ShopProductViewModelOld> getList() {
        return list;
    }

    public void setList(List<ShopProductViewModelOld> list) {
        this.list = list;
    }

    public ShopProductLimitedFeaturedAdapter(ShopProductClickedListener shopProductClickedListener,
                                             ShopProductFeaturedViewHolderOld.ShopProductFeaturedListener shopProductFeaturedListener) {
        this.shopProductClickedListener = shopProductClickedListener;
        this.shopProductFeaturedListener = shopProductFeaturedListener;
        list = new ArrayList<>();
    }

    @Override
    public ShopProductFeaturedViewHolderOld onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ShopProductFeaturedViewHolderOld.LAYOUT, parent, false);
        return new ShopProductFeaturedViewHolderOld(view, shopProductClickedListener, shopProductFeaturedListener);
    }

    @Override
    public void onBindViewHolder(ShopProductFeaturedViewHolderOld holder, int position) {
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
