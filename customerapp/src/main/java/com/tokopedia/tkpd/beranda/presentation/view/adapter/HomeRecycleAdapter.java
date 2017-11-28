package com.tokopedia.tkpd.beranda.presentation.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    protected List<Visitable> items;
    protected final HomeTypeFactory typeFactory;

    public HomeRecycleAdapter() {
        this.typeFactory = new HomeAdapterFactory();
        items = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Visitable> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
