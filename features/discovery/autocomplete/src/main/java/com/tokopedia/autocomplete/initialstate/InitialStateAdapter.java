package com.tokopedia.autocomplete.initialstate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchDataView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InitialStateAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final InitialStateTypeFactory typeFactory;

    public InitialStateAdapter(InitialStateTypeFactory typeFactory) {
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

    @Override
    public void onBindViewHolder(@NotNull AbstractViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(list.get(position), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public void removeSeeMoreButton(int index) {
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void renderRecentSearch(RecentSearchDataView recentSearchDataView, int index) {
        if (index >= 0 && index < list.size()) {
            list.set(index, recentSearchDataView);
            notifyItemChanged(index);
        }
    }

    public void refreshPopularSection(int position) {
        if (position >= 0 && position < (list.size() - 1)) {
            notifyItemRangeChanged(position, 2);
        }
    }
}