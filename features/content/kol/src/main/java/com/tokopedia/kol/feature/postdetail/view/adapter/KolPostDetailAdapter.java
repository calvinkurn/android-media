package com.tokopedia.kol.feature.postdetail.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private KolPostDetailTypeFactory typeFactory;

    @Inject
    public KolPostDetailAdapter() {
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

    public void setTypeFactory(KolPostDetailTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public List<Visitable> getList() {
        return list;
    }
}
