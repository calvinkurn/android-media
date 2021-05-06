package com.tokopedia.explore.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreImageAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final List<Visitable> list;
    private ExploreImageTypeFactory typeFactory;
    private final LoadingMoreModel loadingMoreModel;
    private final EmptyModel emptyModel;

    @Inject
    public ExploreImageAdapter(@ApplicationContext Context context) {
        this.list = new ArrayList<>();
        this.loadingMoreModel = new LoadingMoreModel();
        this.emptyModel = new EmptyModel();
        this.emptyModel.setIconRes(com.tokopedia.feedcomponent.R.drawable.ic_empty_search);
        this.emptyModel.setContent(context.getString(R.string.explore_empty_result));
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    private void add(Visitable visitable) {
        int position = getItemCount();
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

    public void showLoading() {
        add(loadingMoreModel);
    }

    public void dismissLoading() {
        remove(loadingMoreModel);
    }

    public void showEmpty() {
        if (!list.contains(emptyModel)) {
            add(emptyModel);
        }
    }

    public boolean isLoading() {
        return this.list.contains(loadingMoreModel);
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void setTypeFactory(ExploreImageTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public void addList(List<Visitable> list) {
        int position = getItemCount();
        this.list.addAll(list);
        notifyItemRangeInserted(position, list.size());
    }

    public List<Visitable> getList() {
        return list;
    }
}
