package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;

public class ListProductItemViewHolder extends GridProductItemViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_srp_item_list;

    public ListProductItemViewHolder(View itemView, ProductListener itemClickListener) {
        super(itemView, itemClickListener);
    }
}
