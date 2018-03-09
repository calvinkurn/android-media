package com.tokopedia.abstraction.base.view.adapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

public class BaseAdapter<F extends AdapterTypeFactory> extends RecyclerView.Adapter<AbstractViewHolder> {

    protected List<Visitable> visitables;
    private F adapterTypeFactory;
    protected LoadingModel loadingModel = new LoadingModel();
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
        //use last index for performance since loading is in the last item position
        return visitables.lastIndexOf(loadingModel) != -1;
    }

    public void showLoading() {
        //use last index for performance since loading is in the last item position
        // note: do not use flag, because loading model can be removed from anywhere
        if (visitables.lastIndexOf(loadingModel) == -1) {
            loadingModel.setFullScreen(visitables.size() == 0);
            visitables.add(loadingModel);
            notifyItemInserted(visitables.size());
        }
    }

    public void hideLoading() {
        //use last index for performance since loading is in the last item position
        // note: do not use flag, because loading model can be removed from anywhere
        int index = visitables.lastIndexOf(loadingModel);
        if (index != -1) {
            visitables.remove(index);
            notifyItemRemoved(index);
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

    public void setElement(int position, Visitable element) {
        visitables.set(position, element);
        notifyDataSetChanged();
    }

    public void setElements(int position, List<? extends Visitable> data) {
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

    public void setElement(List<? extends Visitable> data) {
        visitables.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAllElements() {
        visitables.clear();
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

    public void setLoadingModel(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }
}