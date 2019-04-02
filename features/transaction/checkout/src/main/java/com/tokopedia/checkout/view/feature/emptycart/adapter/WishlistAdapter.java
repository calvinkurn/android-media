package com.tokopedia.checkout.view.feature.emptycart.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.checkout.view.feature.emptycart.viewholder.WishlistViewHolder;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistViewHolder> {

    private final EmptyCartContract.Presenter presenter;
    private final ActionListener actionListener;
    private final int itemWidth;

    public WishlistAdapter(EmptyCartContract.Presenter presenter, ActionListener actionListener, int itemWidth) {
        this.presenter = presenter;
        this.actionListener = actionListener;
        this.itemWidth = itemWidth;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(WishlistViewHolder.LAYOUT_WISHLIST, parent, false);
        return new WishlistViewHolder(view, actionListener, itemWidth);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        if (presenter != null && presenter.getWishlistViewModels() != null) {
            WishlistViewModel data = presenter.getWishlistViewModels().get(position);
            holder.bindData(data);
        }
    }

    @Override
    public int getItemCount() {
        if (presenter != null && presenter.getWishlistViewModels() != null) {
            return presenter.getWishlistViewModels().size();
        }
        return 0;
    }

    public interface ActionListener {

        void onItemWishListClicked(Wishlist wishlist, int position);

    }
}
