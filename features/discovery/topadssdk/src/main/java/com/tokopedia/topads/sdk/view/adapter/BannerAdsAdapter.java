package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerAdsAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Item> list;
    private BannerAdsTypeFactory typeFactory;

    public BannerAdsAdapter(BannerAdsTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder((ViewGroup) view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
