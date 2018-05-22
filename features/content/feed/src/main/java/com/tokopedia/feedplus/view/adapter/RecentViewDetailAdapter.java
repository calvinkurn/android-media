package com.tokopedia.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.view.adapter.typefactory.recentview.RecentViewTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final RecentViewTypeFactory typeFactory;
    private LoadingModel loadingModel;

    public RecentViewDetailAdapter(RecentViewTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.loadingModel = new LoadingModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);

        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }


    public void showLoading() {
        this.list.add(loadingModel);
        notifyDataSetChanged();
    }

    public void dismissLoading() {
        this.list.remove(loadingModel);
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public void addList(ArrayList<Visitable> list) {
        this.list.addAll(list);
    }

    public List<Visitable> getList() {
        return list;
    }

}
