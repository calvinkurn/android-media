package com.tokopedia.kol.feature.post.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/02/18.
 */

public class KolPostAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> list;
    private final KolPostTypeFactory typeFactory;
    private EmptyKolPostViewModel emptyModel;
    private LoadingModel loadingModel;
    private ErrorNetworkModel errorNetworkModel;
    private ExploreViewModel exploreViewModel;

    @Inject
    public KolPostAdapter(KolPostTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.emptyModel = new EmptyKolPostViewModel();
        this.loadingModel = new LoadingModel();
        this.errorNetworkModel = new ErrorNetworkModel();
        this.exploreViewModel = new ExploreViewModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, viewGroup, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder abstractViewHolder, int i) {
        abstractViewHolder.bind(list.get(i));
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

    public List<Visitable> getList() {
        return this.list;
    }

    public void setList(List<Visitable> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<Visitable> list) {
        int originalSize = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(
                originalSize,
                Math.abs(originalSize - list.size())
        );
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void showEmpty() {
        this.list.add(emptyModel);
        notifyDataSetChanged();
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
        notifyDataSetChanged();
    }

    public void showLoading() {
        this.list.add(loadingModel);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
        notifyDataSetChanged();
    }

    public void showExplore(String kolName) {
        exploreViewModel.setKolName(kolName);
        this.list.add(exploreViewModel);
        notifyDataSetChanged();
    }

    public void removeExplore() {
        this.list.remove(exploreViewModel);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return this.list.contains(emptyModel) || getItemCount() == 0;
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public void showErrorNetwork(String errorMessage,
                                 ErrorNetworkModel.OnRetryListener onRetryListener) {
        errorNetworkModel.setErrorMessage(errorMessage);
        errorNetworkModel.setOnRetryListener(onRetryListener);
        this.list.add(errorNetworkModel);
        notifyDataSetChanged();
    }

    public void removeErrorNetwork() {
        errorNetworkModel.setErrorMessage("");
        errorNetworkModel.setOnRetryListener(null);
        this.list.remove(errorNetworkModel);
        notifyDataSetChanged();
    }
}
