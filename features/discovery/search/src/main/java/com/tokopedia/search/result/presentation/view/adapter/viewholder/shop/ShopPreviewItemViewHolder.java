package com.tokopedia.search.result.presentation.view.adapter.viewholder.shop;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.search.R;

public class ShopPreviewItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView img_preview;

    public ShopPreviewItemViewHolder(View itemView) {
        super(itemView);
        this.img_preview = itemView.findViewById(R.id.img_preview);
    }
}