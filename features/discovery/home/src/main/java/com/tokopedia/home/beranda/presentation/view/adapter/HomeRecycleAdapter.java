package com.tokopedia.home.beranda.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.BaseAdapter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.inspiration.InspirationViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRecycleAdapter extends BaseAdapter {

    protected HomeAdapterFactory typeFactory;
    private EmptyModel emptyModel;
    private RetryModel retryModel;

    public HomeRecycleAdapter(HomeAdapterFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        this.typeFactory = adapterTypeFactory;
        this.emptyModel = new EmptyModel();
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

    public void addItems(List<Visitable> items) {
        this.visitables.addAll(items);
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

    public void showEmpty() {
        this.visitables.add(emptyModel);
    }

    public void removeEmpty() {
        this.visitables.remove(emptyModel);
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

    public int findFirstInspirationPosition() {
        int pos = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItems().get(i) instanceof DigitalsViewModel) {
                pos = (i + 1);
                break;
            }
        }
        return pos;
    }
}
