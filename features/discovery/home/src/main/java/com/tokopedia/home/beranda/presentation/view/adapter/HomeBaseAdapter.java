package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.ListAdapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder;

import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

public class HomeBaseAdapter<F extends AdapterTypeFactory> extends
        ListAdapter<Visitable, AbstractViewHolder> {

    protected List<Visitable> visitables;
    private F adapterTypeFactory;

    protected HomeBaseAdapter(AsyncDifferConfig<Visitable> asyncDifferConfig,
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
        try {
            holder.bind(getItem(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(getItem(position), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
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

