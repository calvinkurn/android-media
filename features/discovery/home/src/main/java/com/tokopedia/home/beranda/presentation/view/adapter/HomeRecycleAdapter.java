package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.RetryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends BaseAdapter<HomeAdapterFactory> {

    protected HomeAdapterFactory typeFactory;
    private RetryModel retryModel;

    public HomeRecycleAdapter(HomeAdapterFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        this.typeFactory = adapterTypeFactory;
        this.retryModel = new RetryModel();
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
        temporaryList.addAll(visitables);
        if (getItems().size() > 1 && getItems().get(1) instanceof HeaderViewModel) {
            temporaryList.add(1, getItems().get(1));
        }

        int firstInspirationPos = findFirstInspirationPosition();
        if (firstInspirationPos < getItemCount()) {
            temporaryList.addAll(getItems().subList(firstInspirationPos, getItemCount()));
        }

        clearItems();
        getItems().addAll(temporaryList);
        notifyDataSetChanged();
    }

    public void showRetry() {
        int positionStart = getItemCount();
        this.visitables.add(retryModel);
        notifyItemRangeInserted(positionStart, 1);
    }

    public void removeRetry() {
        int index = this.visitables.indexOf(retryModel);
        this.visitables.remove(retryModel);
        notifyItemRemoved(index);
    }

    public boolean isRetryShown() {
        return visitables.contains(retryModel);
    }
}
