package com.tokopedia.entertainment.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.entertainment.search.adapter.factory.DetailTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class DetailEventAdapter extends RecyclerView.Adapter<DetailEventViewHolder> {

    public List<DetailEventItem> items;
    private DetailTypeFactory factory;

    public DetailEventAdapter(DetailTypeFactory factory){
        this.items = new ArrayList<>();
        this.factory = factory;
    }

    public void setItems(List<DetailEventItem> list){
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return factory.createViewHolder((ViewGroup) view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type(factory);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailEventViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
