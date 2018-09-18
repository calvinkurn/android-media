package com.tokopedia.affiliate.feature.dashboard.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> itemList;
    private DashboardItemTypeFactory factory;

    @Inject
    public DashboardAdapter(List<Visitable> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return factory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
