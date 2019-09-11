package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.view.adapter.factory.FeedAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feednew.ProductFeedNewViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feednew.ShopFeedNewViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class FeedAdsItemAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Item> list;
    private FeedAdapterTypeFactory typeFactory;
    private int clickPosition;
    private int adapterPosition;

    public FeedAdsItemAdapter(Context context) {
        this.list = new ArrayList<>();
        this.typeFactory = new FeedAdapterTypeFactory(context, clickPosition);
    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        this.list.add(item);
        notifyDataSetChanged();
    }

    public Item getItem(int position) {
        return list.get(position);
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        typeFactory.setItemClickListener(itemClickListener);
        notifyDataSetChanged();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder((ViewGroup) view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
        if (holder instanceof ShopFeedNewViewHolder) {
            ((ShopFeedNewViewHolder) holder).setAdapterPosition(adapterPosition);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull AbstractViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof ShopFeedNewViewHolder) {
            ((ShopFeedNewViewHolder) holder).onViewRecycled();
        } else if (holder instanceof ProductFeedNewViewHolder) {
            ((ProductFeedNewViewHolder) holder).onViewRecycled();
        }
    }

    public void clearData() {
        list.clear();
    }

    public void setPosition(int adapterPosition) {
        typeFactory.setClickPosition(adapterPosition);
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}
