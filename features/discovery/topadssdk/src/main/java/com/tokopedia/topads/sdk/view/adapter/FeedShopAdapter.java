package com.tokopedia.topads.sdk.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class FeedShopAdapter extends RecyclerView.Adapter<FeedShopAdapter.ViewHolder> {

    private static final int MAX_SIZE = 3;

    private List<ImageProduct> list;
    private ImageLoader imageLoader;
    private View.OnClickListener itemClickListener;

    public FeedShopAdapter(View.OnClickListener itemClickListener) {
        this.list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public FeedShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topads_shop_product_image_feed, parent, false);
        return new FeedShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedShopAdapter.ViewHolder holder, int position) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(holder.imageView.getContext());
        }
        imageLoader.loadImage(list.get(position).getImageUrl(), holder.imageView);
        holder.imageView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        if (list.size() > MAX_SIZE) {
            return MAX_SIZE;
        }

        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

        ImageLoader.clearImage(holder.imageView);
    }

    public List<ImageProduct> getList() {
        return list;
    }

    public void setList(List<ImageProduct> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }
}
