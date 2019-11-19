package com.tokopedia.recentview.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final List<Visitable> list;
    private final RecentViewTypeFactory typeFactory;
    private final LoadingModel loadingModel;
    private final EmptyResultViewModel emptyResultViewModel;

    public RecentViewDetailAdapter(RecentViewTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.loadingModel = new LoadingModel();
        this.emptyResultViewModel = new EmptyResultViewModel();
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

    private void add(Visitable visitable) {
        int position = this.list.size();
        if (this.list.add(visitable)) {
            notifyItemInserted(position);
        }
    }

    private void remove(Visitable visitable) {
        int position = this.list.indexOf(visitable);
        if (this.list.remove(visitable)) {
            notifyItemRemoved(position);
        }
    }

    public void showEmpty() {
        if (!this.list.contains(emptyResultViewModel)) {
            add(emptyResultViewModel);
        }
    }

    public void hideEmpty() {
        if (this.list.contains(emptyResultViewModel)) {
            remove(emptyResultViewModel);
        }
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
