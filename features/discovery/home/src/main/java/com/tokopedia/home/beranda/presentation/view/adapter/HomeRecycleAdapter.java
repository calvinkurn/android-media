package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends BaseAdapter<HomeAdapterFactory> {

    protected HomeAdapterFactory typeFactory;

    public HomeRecycleAdapter(HomeAdapterFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        this.typeFactory = adapterTypeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return visitables.size();
    }

    public void setItems(List<Visitable> items) {
        this.visitables = items;
        notifyDataSetChanged();
    }

    public Visitable getItem(int pos) {
        return visitables.get(pos);
    }

    public List<Visitable> getItems() {
        return visitables;
    }

    public void clearItems() {
        visitables.clear();
    }

    public int findFirstInspirationPosition() {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItems().get(i) instanceof TopAdsViewModel
                    || getItems().get(i) instanceof InspirationViewModel) {
                return i;
            }
        }
        return getItemCount();
    }

    public void updateItems(List<Visitable> visitables) {
        List<Visitable> temporaryList = new ArrayList<>();
        if (getItems().get(0) instanceof HeaderViewModel) {
            temporaryList.add(getItems().get(0));
        }

        temporaryList.addAll(visitables);

        int firstInspirationPos = findFirstInspirationPosition();
        if (firstInspirationPos < getItemCount()) {
            temporaryList.addAll(getItems().subList(firstInspirationPos, getItemCount()));
        }

        clearItems();
        getItems().addAll(temporaryList);
        notifyDataSetChanged();
    }
}
