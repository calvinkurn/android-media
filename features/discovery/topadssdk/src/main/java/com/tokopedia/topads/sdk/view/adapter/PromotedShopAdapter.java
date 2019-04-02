package com.tokopedia.topads.sdk.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feed.ShopFeedViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 5/17/17.
 */

public class PromotedShopAdapter extends RecyclerView.Adapter<PromotedShopAdapter.ViewHolder> {

    List<ImageProduct> list;
    ImageLoader imageLoader;
    private int MAX_SIZE = 3;
    ShopFeedViewHolder viewHolder;

    public PromotedShopAdapter(ShopFeedViewHolder shopFeedViewHolder) {
        this.list = new ArrayList<>();
        viewHolder = shopFeedViewHolder;
    }

    @Override
    public PromotedShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topads_shop_product_image, parent, false);
        return new PromotedShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromotedShopAdapter.ViewHolder holder, int position) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(holder.imageView.getContext());
        }
        imageLoader.loadImage(list.get(position).getImageUrl(), holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.onClick(R.id.header);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ImageProduct> list) {
        this.list.clear();
        for (int i = 0; i < MAX_SIZE; i++) {
            this.list.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public List<ImageProduct> getList() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }
}
