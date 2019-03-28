package com.tokopedia.abstraction.base.view.adapter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

public class BaseAdapter<F extends AdapterTypeFactory> extends RecyclerView.Adapter<AbstractViewHolder> {

    protected List<Visitable> visitables;
    private F adapterTypeFactory;
    protected LoadingModel loadingModel = new LoadingModel();
    protected LoadingMoreModel loadingMoreModel = new LoadingMoreModel();
    protected ErrorNetworkModel errorNetworkModel = new ErrorNetworkModel();

    public BaseAdapter(F adapterTypeFactory, List<Visitable> visitables) {
        this.adapterTypeFactory = adapterTypeFactory;
        this.visitables = visitables;
    }

    public BaseAdapter(F adapterTypeFactory) {
        this(adapterTypeFactory, new ArrayList<Visitable>());
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
        holder.bind(visitables.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(visitables.get(position), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return visitables.size();
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

    public List<Visitable> getList() {
        return visitables;
    }

    public boolean isLoading() {
        int lastIndex = getLastIndex();
        if (lastIndex > -1) {
            return visitables.get(lastIndex) instanceof LoadingModel ||
                    visitables.get(lastIndex) instanceof LoadingMoreModel;
        } else {
            return false;
        }
    }

    public void showLoading() {
        if (!isLoading()) {
            if (isShowLoadingMore()) {
                visitables.add(loadingMoreModel);
            } else {
                visitables.add(loadingModel);
            }
            notifyItemInserted(visitables.size());
        }
    }

    protected boolean isShowLoadingMore() {
        return visitables.size() > 0;
    }

    public int getFirstIndex() {
        int size = visitables.size();
        if (size > 0) {
            return 0;
        }
        return -1;
    }

    public int getLastIndex() {
        int size = visitables.size();
        if (size > 0) {
            return size - 1;
        }
        return -1;
    }

    public void hideLoading() {
        if (isLoading()) {
            int lastIndex = getLastIndex();
            visitables.remove(getLastIndex());
            notifyItemRemoved(lastIndex);
        }
    }

    public void clearElement(Visitable visitable) {
        int index = visitables.indexOf(visitable);
        if (index != -1) {
            visitables.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void showErrorNetwork() {
        visitables.clear();
        visitables.add(errorNetworkModel);
        notifyDataSetChanged();
    }

    public ErrorNetworkModel getErrorNetworkModel() {
        return errorNetworkModel;
    }

    public void setErrorNetworkModel(ErrorNetworkModel errorNetworkModel) {
        this.errorNetworkModel = errorNetworkModel;
    }

    public void showErrorNetwork(String message, ErrorNetworkModel.OnRetryListener onRetryListener) {
        errorNetworkModel.setErrorMessage(message);
        errorNetworkModel.setOnRetryListener(onRetryListener);
        showErrorNetwork();
    }

    public void removeErrorNetwork() {
        visitables.remove(errorNetworkModel);
        notifyDataSetChanged();
    }

    public void addElement(List<? extends Visitable> visitables) {
        this.visitables.addAll(visitables);
        notifyDataSetChanged();
    }

    public void setVisitables(List<Visitable> visitables) {
        this.visitables = visitables;
        notifyDataSetChanged();
    }

    public void setElement(int position, Visitable element) {
        visitables.set(position, element);
        notifyDataSetChanged();
    }

    public void setElements(List<Visitable> data) {
        visitables = data;
        notifyDataSetChanged();
    }

    public void addElement(int position, Visitable element) {
        visitables.add(position, element);
        notifyDataSetChanged();
    }

    public void addElement(Visitable element) {
        visitables.add(element);
        notifyDataSetChanged();
    }

    public void setElement(List<? extends Visitable> data) {
        visitables.addAll(data);
        notifyDataSetChanged();
    }

    public void setElement(Visitable data) {
        List<Visitable> buffer = new ArrayList<>();
        buffer.add(data);
        visitables = buffer;
        notifyDataSetChanged();
    }

    public void clearAllElements() {
        visitables.clear();
        notifyDataSetChanged();
    }

    public void softClear() {
        visitables = new ArrayList<>();
    }

    public void addMoreData(List<? extends Visitable> data) {
        final int positionStart = visitables.size();
        visitables.addAll(data);
        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionStart, data.size());
        }
    }


    public void removeElement(Visitable visitable) {
        visitables.remove(visitable);
        notifyDataSetChanged();
    }

    public void setLoadingModel(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }
}