package com.tokopedia.entertainment.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory;
import com.tokopedia.entertainment.home.data.EventFavoriteResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Author errysuprayogi on 30,January,2020
 */
public class HomeEventAdapter extends RecyclerView.Adapter<HomeEventViewHolder> {

    public List<HomeEventItem> items;
    private HomeTypeFactory factory;

    public HomeEventAdapter(HomeTypeFactory factory) {
        this.items = new ArrayList<>();
        this.factory = factory;
    }

    public void setItems(List<HomeEventItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return factory.createViewHolder((ViewGroup) view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type(factory);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeEventViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
