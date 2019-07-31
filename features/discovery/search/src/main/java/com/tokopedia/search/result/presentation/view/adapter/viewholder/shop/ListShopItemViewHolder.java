package com.tokopedia.search.result.presentation.view.adapter.viewholder.shop;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ShopListItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.ShopListener;


public class ListShopItemViewHolder extends GridShopItemViewHolder {

    private Context context;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_shop_list;

    public ListShopItemViewHolder(View itemView, ShopListener itemClickListener) {
        super(itemView, itemClickListener);
        this.context = itemView.getContext();
    }

    protected RecyclerView.ItemDecoration getDecoration() {
        return new ShopListItemDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_4),
                context.getResources().getDimensionPixelSize(R.dimen.dp_4),
                0,
                0);
    }

    @Override
    protected int getPreviewImageSize(Context context) {
        return (int)context.getResources().getDimension(R.dimen.shop_item_preview_size);
    }

    @Override
    protected void hideShopPreviewItems(View viewPreviewItems) {
        viewPreviewItems.setVisibility(View.GONE);
    }
}