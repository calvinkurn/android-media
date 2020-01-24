package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends HomeBaseAdapter<HomeAdapterFactory> {
    protected HomeAdapterFactory typeFactory;

    public HomeRecycleAdapter(AsyncDifferConfig<HomeVisitable> asyncDifferConfig, HomeAdapterFactory adapterTypeFactory,
                              List<Visitable> visitables) {
        super(asyncDifferConfig, adapterTypeFactory, visitables);
        typeFactory = adapterTypeFactory;
        this.visitables = visitables;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type(typeFactory);
    }
}
