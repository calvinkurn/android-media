package com.tokopedia.core.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.model.ErrorNetworkModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;

import java.util.List;

/**
 * @author by erry on 02/02/17.
 */

/**
 * Use BaseAdapter (visitable pattern) from tkpd abstraction
 */
@Deprecated
public class BaseAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private LoadingModel loadingModel = new LoadingModel();
    private ErrorNetworkModel errorNetworkModel = new ErrorNetworkModel();
    protected List<Visitable> visitables;
    protected BaseAdapterTypeFactory adapterTypeFactory;

    public BaseAdapter(BaseAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
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
}