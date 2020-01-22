package com.tokopedia.home.beranda.presentation.view.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder;
import com.tokopedia.home.beranda.domain.model.HomeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

public class HomeBaseAdapter<F extends AdapterTypeFactory> extends
        ListAdapter<HomeVisitable, AbstractViewHolder> {

    protected List<Visitable> visitables;
    private F adapterTypeFactory;

    protected HomeBaseAdapter(AsyncDifferConfig<HomeVisitable> asyncDifferConfig,
                              F adapterTypeFactory, List<Visitable> visitables) {
        super(asyncDifferConfig);
        this.adapterTypeFactory = adapterTypeFactory;
        this.visitables = visitables;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = onCreateViewItem(parent, viewType);
        return adapterTypeFactory.createViewHolder(view, viewType);
    }

    protected View onCreateViewItem(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Bundle && !((Bundle) payloads.get(0)).isEmpty()) {
                holder.bind(getItem(position), payloads);
            }
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        if (position < 0 || position >= visitables.size()) {
            return HideViewHolder.LAYOUT;
        }
        return visitables.get(position).type(adapterTypeFactory);
    }

    @Override
    public void onViewRecycled(@NonNull AbstractViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}

