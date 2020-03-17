package com.tokopedia.entertainment.search.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchEventAdapter extends RecyclerView.Adapter<SearchEventViewHolder> {

    public List<SearchEventItem> items;
    private SearchTypeFactory factory;

    public SearchEventAdapter(SearchTypeFactory factory) {
        this.items = new ArrayList<>();
        this.factory = factory;
    }

    public void setItems(List<SearchEventItem> items) {
        Log.d("SearchEventAdapter", "setitem "+items.size());
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return factory.createViewHolder((ViewGroup) view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type(factory);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchEventViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
