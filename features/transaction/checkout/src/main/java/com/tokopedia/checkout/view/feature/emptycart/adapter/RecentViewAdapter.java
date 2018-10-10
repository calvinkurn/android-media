package com.tokopedia.checkout.view.feature.emptycart.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.checkout.view.feature.emptycart.viewholder.RecentViewViewHolder;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.RecentViewViewModel;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class RecentViewAdapter extends RecyclerView.Adapter<RecentViewViewHolder> {

    private final EmptyCartContract.Presenter presenter;
    private final ActionListener actionListener;
    private final int itemWidth;

    public RecentViewAdapter(EmptyCartContract.Presenter presenter, ActionListener actionListener, int itemWidth) {
        this.presenter = presenter;
        this.actionListener = actionListener;
        this.itemWidth = itemWidth;
    }

    @NonNull
    @Override
    public RecentViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(RecentViewViewHolder.LAYOUT_RECENT_VIEW, parent, false);
        return new RecentViewViewHolder(view, actionListener, itemWidth);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewViewHolder holder, int position) {
        if (presenter != null && presenter.getRecentViewListModels() != null) {
            RecentViewViewModel data = presenter.getRecentViewListModels().get(position);
            holder.bindData(data);
        }
    }

    @Override
    public int getItemCount() {
        if (presenter != null && presenter.getRecentViewListModels() != null) {
            return presenter.getRecentViewListModels().size();
        }
        return 0;
    }

    public interface ActionListener {

        void onItemRecentViewClicked(RecentView recentView, int position);

    }

}
