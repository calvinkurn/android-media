/*
 * Created By Kulomady on 11/26/16 1:08 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 1:08 AM
 */

package com.tokopedia.discovery.adapter.custom;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.core.InfoTopAds;
import com.tokopedia.core.R;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.adapter.ProductAdapter;

import java.util.List;

/**
 * Created by normansyahputa on 10/3/16.
 */

public class TopAdsListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "TopAdsList";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<ProductItem> data;
    private Context context;
    private String source;

    public TopAdsListRecyclerViewAdapter(List<ProductItem> data, Context context, String source) {
        this.data = data;
        this.context = context;
        this.source = source;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            return new ProductAdapter.ViewHolderProductitem(context, LayoutInflater.from(context).inflate(R.layout.listview_product_item_list, parent, false));
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            return new VHHeader(LayoutInflater.from(context).inflate(R.layout.child_main_top_ads_header, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductAdapter.ViewHolderProductitem) {
            ProductAdapter.ViewHolderProductitem holderProductitem = (ProductAdapter.ViewHolderProductitem) holder;
            holderProductitem.bindData(getItem(position), holderProductitem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private ProductItem getItem(int position) {
        return data.get(position - 1);
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView infoTopAds;

        public VHHeader(View itemView) {
            super(itemView);
            infoTopAds = (ImageView) itemView.findViewById(R.id.info_topads);
            infoTopAds.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            InfoTopAds infoTopAds = InfoTopAds.newInstance(source);
            Activity activity = (Activity) context;
            infoTopAds.show(activity.getFragmentManager(), "INFO_TOPADS");
        }
    }
}
