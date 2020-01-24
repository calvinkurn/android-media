package com.tokopedia.home.beranda.presentation.view.adapter;

import android.os.Bundle;
import android.util.Log;
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
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeAbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

public class HomeBaseAdapter<F extends HomeTypeFactory> extends
        ListAdapter<HomeVisitable, HomeAbstractViewHolder> {

    protected List<Visitable> visitables;
    private F adapterTypeFactory;

    protected HomeBaseAdapter(AsyncDifferConfig<HomeVisitable> asyncDifferConfig,
                              F adapterTypeFactory, List<Visitable> visitables) {
        super(asyncDifferConfig);
        this.adapterTypeFactory = adapterTypeFactory;
        this.visitables = visitables;
    }

    @Override
    public HomeAbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = onCreateViewItem(parent, viewType);
        return adapterTypeFactory.createViewHolder(view, viewType);
    }

    protected View onCreateViewItem(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(HomeAbstractViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull HomeAbstractViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(getItem(position), payloads);
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
    public void onViewRecycled(@NonNull HomeAbstractViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull HomeAbstractViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }
}

