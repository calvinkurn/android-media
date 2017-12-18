package com.tokopedia.abstraction.base.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

public class BaseAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    protected List<Visitable> visitables;
    protected AdapterTypeFactory adapterTypeFactory;
    private Visitable loadingModel = new LoadingModel();
    private Visitable errorNetworkModel = new ErrorNetworkModel();

    public BaseAdapter(AdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        this.adapterTypeFactory = adapterTypeFactory;
        this.visitables = visitables;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return adapterTypeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
    }

    @Override
    public int getItemCount() {
        return visitables.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(adapterTypeFactory);
    }

    public boolean isLoading() {
        return visitables.contains(loadingModel);
    }

    public void showLoading() {
        if (!visitables.contains(loadingModel)) {
            visitables.add(loadingModel);
            notifyItemInserted(visitables.size() + 1);
        }
    }

    public void hideLoading() {
        if (visitables.contains(loadingModel)) {
            visitables.remove(loadingModel);
            notifyItemRemoved(visitables.size());
        }
    }

    public void showErrorNetwork() {
        visitables.clear();
        visitables.add(errorNetworkModel);
        notifyDataSetChanged();
    }

    public void removeErrorNetwork() {
        visitables.remove(errorNetworkModel);
        notifyDataSetChanged();
    }

    public void addElement(List<Visitable> visitables) {
        this.visitables.addAll(visitables);
        notifyDataSetChanged();
    }

    public void setElement(int position, Visitable element) {
        visitables.set(position, element);
        notifyDataSetChanged();
    }

    public void setElements(int position, List<Visitable> data) {
        visitables.addAll(position, data);
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

    public void setElement(List<Visitable> data) {
        visitables.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        visitables.clear();
    }

    public void addMoreData(List<Visitable> data) {
        final int positionStart = visitables.size() + 1;
        visitables.addAll(data);
        notifyItemRangeInserted(positionStart, data.size());
    }
}