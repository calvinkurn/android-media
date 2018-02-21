package com.tokopedia.tkpdcontent.feature.profile.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory.KolTypeFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 19/02/18.
 */

public class KolPostAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> list;
    private final KolTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;

    @Inject
    public KolPostAdapter(KolTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(i, viewGroup, false);
        return typeFactory.createViewHolder(view, i);
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

    public void setList(List<Visitable> list) {
        this.list = list;
    }

    public void addList(List<Visitable> list) {
        this.list.addAll(list);
    }

    public void addItem(Visitable item) {
        this.list.add(item);
    }

    public void clearData() {
        this.list.clear();
    }

    public void showEmpty() {
        this.list.add(emptyModel);
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
    }

    public void showLoading() {
        this.list.add(loadingModel);
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }
}
