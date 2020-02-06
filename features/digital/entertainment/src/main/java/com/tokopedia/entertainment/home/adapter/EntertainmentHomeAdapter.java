package com.tokopedia.entertainment.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory;

import java.util.List;

/**
 * Author errysuprayogi on 30,January,2020
 */
public class EntertainmentHomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final List<HomeItem> items;
    private HomeTypeFactory factory;

    public EntertainmentHomeAdapter(HomeTypeFactory factory, List<HomeItem> items) {
        this.items = items;
        this.factory = factory;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return factory.createViewHolder((ViewGroup) view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type(factory);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
