package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InspirationCarouselOptionAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final InspirationCarouselOptionAdapterTypeFactory typeFactory;

    public InspirationCarouselOptionAdapter(InspirationCarouselOptionAdapterTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
    }

    @NotNull
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
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<Visitable> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
    }
}
