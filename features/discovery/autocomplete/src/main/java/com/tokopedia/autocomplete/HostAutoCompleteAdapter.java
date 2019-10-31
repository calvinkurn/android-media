package com.tokopedia.autocomplete;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HostAutoCompleteAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final HostAutoCompleteTypeFactory typeFactory;
    private List<Visitable> list;

    public HostAutoCompleteAdapter(HostAutoCompleteTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.list.add(new DefaultAutoCompleteViewModel());
        this.list.add(new TabAutoCompleteViewModel());
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

    public void setDefaultViewModel(DefaultAutoCompleteViewModel model) {
        this.list.set(0, model);
        notifyDataSetChanged();
    }

    public void setSuggestionViewModel(TabAutoCompleteViewModel model) {
        this.list.set(1, model);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
